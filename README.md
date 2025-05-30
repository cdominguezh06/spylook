# SpyLook 👀

SpyLook es una aplicación Android desarrollada en `Kotlin` y utilizando el sistema de Views en `XML` para apuntar información sobre las personas de tu entorno. Desde sus datos personales hasta sus amistades, grupos, cuentas de *RRSS*, etc

## Instalación

Puedes descargar cualquier versión de la aplicación directamente en formato APK desde la web oficial:  
[https://cdominguezh06.github.io/spylook-web/](https://cdominguezh06.github.io/spylook-web/)

## Estructura de la aplicación

```
spylook/
 ├── adapters/        # Adaptadores para listas y componentes de UI (RecyclerView, etc.)
 ├── controller/      # Controladores para acceso a APIs REST de terceros
 ├── dao/             # Objetos de acceso a datos (DAOs) para la base de datos local de Android Room
 ├── database/        # Definición y configuración de la base de datos de Android Room
 ├── mappers/         # Conversores entre modelos de la base de datos y 'tarjetas' (elementos a mostrar en listas de RecyclerView)
 ├── model/           # Modelos de datos: entidades como Contacto, Grupo, Suceso, etc.
 └── view/            # Activities, fragments y vistas principales organizadas en subcarpetas
      ├── accounts/   # Vistas relativas al CRUD de cuentas
      ├── common/     # Vistas y fragments comunes o reutilizables 
      ├── contacts/   # Vistas relativas al CRUD de contactos
      ├── groups/     # Vistas relativas al CRUD de grupos
      └── sucesos/    # Vistas relativas al CRUD de sucesos
```
### Descripción de carpetas clave

- **adapters/**
  ```
    adapters/
  ├── cards/
  │    ├── AmigoCardAdapter.kt
  │    ├── AnotacionCardAdapter.kt
  │    ├── ContactoCardAdapter.kt
  │    ├── ContactoMiniCardAdapter.kt
  │    ├── CuentaCardAdapter.kt
  │    ├── GrupoCardAdapter.kt
  │    ├── GruposDeContactoCardAdapter.kt
  │    └── SucesoCardAdapter.kt
  ├── search/
  │    ├── BusquedaContactoCardAdapter.kt
  │    ├── BusquedaGrupoCardAdapter.kt
  │    ├── MultipleContactsCardSearchAdapter.kt
  │    └── SingleContactCardSearchAdapter.kt
  └── slider/
       ├── ContactSliderCardAdapter.kt
       ├── CuentaSliderCardAdapter.kt
       ├── GroupSliderCardAdapter.kt
       └── SucesoSliderCardAdapter.kt
  ```
  Este paquete, dividido en tres paquetes, contiene todos los adaptadores de RecyclerView de la aplicación, 
  cada uno para un caso de uso concreto donde se necesitan listar elementos distintos, con vistas distintas y comportamientos distintos

  A su vez también almacena adaptadores para el comportamiento de los menús de los Fragments
  - **cards/**
    Estos adapters se encargan de mostrar todas las tarjetas de la aplicación. Su función es redirigir al usuario a la activity
    del modelo en cuestión que maneje el adapter para visualizar los datos o mantener pulsada la tarjeta para acceder a información adicional
  - **search/**
    Estos adapters muestran las tarjetas en un contexto de búsqueda (cuando el usuario utiliza el buscador para filtrar rápidamente elementos) y le asignan
    un comportamiento diferente al dibujarlas en pantalla y al interactuar con ellas
  - **slider**
    Estos adaptadores se aplican a un ViewPager para asignar el comportamiento del TabLayout de una activity, definiendo sus pestañas y el fragmento que se inflará
    en cada una

  ---
- **controller/**  
  ```
  controller/
  └── GithubController.kt
  ```
  Controlador para la integración con GitHub (gestión de releases, actualizaciones, etc).

  ---
- **dao/**  
  ```
  dao/
  ├── AnotacionDAO.kt
  ├── ContactoDAO.kt
  ├── CuentaDao.kt
  ├── GrupoDAO.kt
  └── SucesoDAO.kt
  ```
  DAOs para todas las entidades principales: Anotación, Contacto, Cuenta, Grupo, Suceso.

  El acceso a las operaciones de los DAOs se debe realizar de forma asíncrona, evitando así bloquear
  el hilo principal encargado de la UI

  La elección de Kotlin como lenguaje de programación permite realizar esto de forma muy sencilla mediante el uso del
  patrón de diseño asíncrono de las **[Corrutinas](https://developer.android.com/kotlin/coroutines)**

  Las corrutinas son muy similares en concepto a los **[hilos virtuales](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-2DDA5807-5BD5-4ABC-B62A-A1230F0566E0)** de Java. Este concepto consiste en separar 
  la equivalencia de un hilo de la máquina virtual de Java con un hilo del procesador, almacenandose en el heap en lugar de en el stack y permitiendo cambios de contexto con un coste mínimo de rendimiento
  (cuando se pausa la ejecución de una corrutina el hilo físico no se detiene, sino que se le asigna otra tarea)

  En SpyLook se hace uso principalmente de `lifecycleScope.launch` para lanzar corrutinas y acceder a la base de datos. De esta forma se genera una corrutina ligada al ciclo de vida de la Activity o Fragment
  donde se haya creado (si la Activity se cierra la corrutina también) y permite acceder a la base de datos sin bloquear el hilo principal
  ```kotlin
     lifecycleScope.launch {
         val contact = contactoDAO.findContactoById(intent.getIntExtra("id", 0))
         setupContactDetails(contact)
         setupViewPager(contact)
     }
  ```
  Sin embargo, en contextos ajenos a una Activity, como puede ser un adapter de RecyclerView, no se puede hacer uso de `lifecycleScope.launch`, por lo que se recurre al método `runBlocking`
  que genera una corrutina bloqueante, aunque todavía no ha demostrado ser perjudicial para el rendimiento de la app
  ```kotlin
     runBlocking {
         dao.deleteContactoWithAnotableById(cardItem.idAnotable)
         val index = cardItemList.indexOf(cardItem)
         recyclerAnimator.deleteItemWithAnimation(
             holder.itemView,
             index,
             onEmptyCallback = {
                 cardItemList.add(ContactoCardItem.DEFAULT_FOR_EMPTY_LIST)
             },
             afterDeleteCallBack = {
                 popupWindow.dismiss()
             })
     }
  ```
  ---
- **database/**  
  ```
  database/
  └── AppDatabase.kt
  ```
  Definición de la base de datos Room.
  Esta clase cuenta con un patrón *[Singleton](https://refactoring.guru/design-patterns/singleton)* para acceder de forma global a la misma instancia de la base de datos.
  En ella nos encontramos una instancia de cada DAO declarado, la declaración de todas las entidades que serán convertidas en tablas y una versión de la base de datos.
  Esta versión ha de cambiarse cada vez que modificamos el esquema de la base de datos (modificando entidades) para que Room rehaga la base de datos y no acceda a un esquema antiguo, provocando un fallo en el proceso

  ---
- **mappers/**  
  ```
  mappers/
  ├── AnotacionToCardItem.kt
  ├── ContactoToCardItem.kt
  ├── ContactoToMiniCard.kt
  ├── CuentaToCardItem.kt
  ├── GrupoToCardItem.kt
  └── SucesoToCardItem.kt
  ```
  Mappers creados con *[MapStruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct/1.5.3.Final)* para convertir fácilmente entidades en tarjetas para mostrar en un RecyclerView

  ---
- **model/**  
  ```
  model/
  ├── cards/
  │    ├── AnotacionCardItem.kt
  │    ├── ContactoCardItem.kt
  │    ├── ContactoMiniCard.kt
  │    ├── CuentaCardItem.kt
  │    ├── GrupoCardItem.kt
  │    └── SucesoCardItem.kt
  ├── entity/
  │    ├── Anotable.kt
  │    ├── Anotacion.kt
  │    ├── Contacto.kt
  │    ├── ContactoAmistadCrossRef.kt
  │    ├── ContactoGrupoCrossRef.kt
  │    ├── ContactoSucesoCrossRef.kt
  │    ├── Cuenta.kt
  │    ├── CuentaContactoCrossRef.kt
  │    ├── Grupo.kt
  │    └── Suceso.kt
  ├── github/
  │    ├── GitHubAPI.kt
  │    ├── GitHubRelease.kt
  │    └── ReleaseAsset.kt
  ├── relations/
  │    ├── AmigosDeContacto.kt
  │    ├── AnotableConAnotaciones.kt
  │    ├── CausanteSuceso.kt
  │    ├── ContactoConCuentas.kt
  │    ├── ContactosGrupos.kt
  │    ├── ContactosSucesos.kt
  │    ├── CreadorGrupo.kt
  │    ├── CuentaConContactos.kt
  │    ├── GruposContactos.kt
  │    └── SucesosContactos.kt
  └── utils/
       ├── animations/
       ├── converters/
       ├── decorators/
       ├── textWatchers/
       ├── ApplicationUpdater.kt
       ├── ForegroundShaderSpan.kt
       └── StringWithSpacesIndexRetriever.kt
  ```
  En este paquete se encuentran la mayoría de clases de la aplicación
  
  - **cards/**: Clases para cada tipo de tarjeta que se pueda mostrar en un RecyclerView
  - **entity/**: Entidades Room, relaciones entre entidades y crossrefs para relaciones N:M.
  - **github/**: Modelos para integración con la API de GitHub y tomar información de cada release de la aplicación
  - **relations/**: Clases para relaciones entre entidades.
  - **utils/**: Utilidades generales (actualización, decoradores, animaciones, etc), con subcarpetas especializadas.
    ```
    utils/
       ├── animations/
       │    └── RecyclerViewAnimator.kt
       ├── converters/
       │    └── DateConverters.kt
       ├── decorators/
       │    ├── RainbowTextViewDecorator.kt
       │    └── SpacingItemDecoration.kt
       ├── textWatchers/
       │    ├── actions/
       │    │    └── LongTextScrollerAction.kt
       │    ├── DateTextWatcher.kt
       │    ├── TextWatcherSearchBarContacts.kt
       │    ├── TextWatcherSearchBarGroups.kt
       │    ├── TextWatcherSearchBarGruposDeContacto.kt
       │    └── TextWatcherSearchBarMiembros.kt
       ├── ApplicationUpdater.kt
       ├── ForegroundShaderSpan.kt
       └── StringWithSpacesIndexRetriever
    ```
    - **animations/RecyclerViewAnimator.kt**: Animación al eliminar un elemento de un RecyclerView, aceptando funciones anónimas (lambda) como parámetro para definir el comportamiento
      después del borrado y cuando la lista queda vacía tras el borrado
    - **converters/DateConverters.kt**: Singleton para generar conversores de fechas LocalDate o LocalDateTime a String, siguiendo un regex específico
    - **decorators/**: Objetos que alteran la apariencia de elementos de la UI:
      - **RainbowTextViewDecorator.kt**: A la hora de realizar una búsqueda, resalta la parte coincidente del texto con un gradiente de arcoíris
      - **SpacingItemDecoration.kt**: Añade una pequeña separación entre elementos de un RecyclerView
    - **textWatchers/**: Aqui hay objetos que analizan constantemente los cambios de texto de un EditText con distintas finalizades
      - **actions/**: Acciones a aplicar por un TextWatcher sobre un elemento de la UI para alterar su estilo vía código Kotlin
        -**LongTextScrollerAction.kt**: Comprueba si el texto de la búsqueda no cabe en los límites de su respectivo TextView para desplazarlo hasta poder ver la coincidencia
      - **DateTextWatcher.kt**: Comprueba el valor introducido en el EditText asignado y delimita un valor mínimo (01/01/1970) y un valor máximo (LocalDate.now())
      - **TextWatcherSearchBar%.kt**: Estos archivos asignan un comportamiento distinto a la barra de búsqueda según el resultado deseado
    - **ApplicationUpdater.kt**: Singleton que genera la notificación de descarga y la petición de permisos de instalación de orígenes desconocidos
    - **ForegroundShaderSpan.kt**: Aplica el shader de texto arcoíris al Spannable al que entra como parámetro
    - **StringWithSpacesIndexRetriever.kt**: Permite que la búsqueda se pueda realizar sin escribir espacios manteniendo el shader de arcoíris bien aplicado
   
  ---
- **view/**  
  - **accounts/**
    ```
    view/accounts/
    ├── CuentaActivity.kt
    ├── NuevaCuentaActivity.kt
    └── fragments/
    ```
    Activities y fragments para cuentas y gestión de usuario.

  - **common/**
    ```
    view/common/
    ├── MainActivity.kt
    └── fragments/
    ```
    Activity principal (`MainActivity.kt`) y fragments comunes.

  - **contacts/**
    ```
    view/contacts/
    ├── ContactoActivity.kt
    ├── NuevoContactoActivity.kt
    └── fragments/
    ```
    Activities y fragments para contactos.

  - **groups/**
    ```
    view/groups/
    ├── GrupoActivity.kt
    ├── NuevoGrupoActivity.kt
    └── fragments/
    ```
    Activities y fragments para grupos.

  - **sucesos/**
    ```
    view/sucesos/
    ├── NuevoSucesoActivity.kt
    ├── SucesoActivity.kt
    └── fragments/
    ```
    Activities y fragments para sucesos/eventos.

---

### Activities y vistas principales

- **MainActivity**  
  Es la pantalla principal de la aplicación. Desde aquí se navega hacia los distintos apartados de la agenda (personas, grupos, sucesos, etc). Gestiona la barra de búsqueda y la navegación principal.

- **Grupos**
  - `GrupoActivity.kt`: Muestra la información detallada de un grupo, listando sus miembros y permitiendo acciones sobre ellos.
  - `NuevoGrupoActivity.kt`: Permite crear un nuevo grupo, seleccionar miembros y asignarles atributos.

- **Personas / Contactos**  
  *(No se pudo listar el contenido exacto, pero generalmente aquí están las Activities para ver y editar personas, y listados de contactos.)*

- **Sucesos**
  *(También se gestionan desde activities en la carpeta sucesos, encargados de mostrar y crear sucesos relacionados con personas o grupos.)*

- **Accounts y Common**
  - Carpeta `accounts`: Aquí suelen estar las vistas y lógica para la gestión de cuentas de usuario (login, registro, perfil, etc).
  - Carpeta `common`: Contiene componentes reutilizables, fragments y vistas comunes a varias partes de la app.

### Base de datos

La base de datos está organizada en los siguientes paquetes:

- **database**: Aquí se define la base de datos principal y su configuración (probablemente usando Room).
- **dao**: Contiene los objetos de acceso a datos (DAOs), que encapsulan las operaciones CRUD sobre las entidades principales: personas, grupos, sucesos, etc.
- **model**: Define las entidades de la base de datos, como Persona, Grupo, Suceso y otras relacionadas.

Esto permite mantener los datos almacenados localmente y consultarlos de forma eficiente desde las distintas actividades.

### Barra de búsqueda

La barra de búsqueda está integrada en la pantalla principal (`MainActivity`) y probablemente también en otras vistas de listado. Permite buscar personas, grupos o sucesos de forma rápida, filtrando los resultados mientras el usuario escribe.

### Otros componentes relevantes

- **adapters**: Aquí se encuentran los Adapter de RecyclerView para mostrar listados de personas, grupos o sucesos.
- **controller y mappers**: Encargados de la lógica de negocio y de transformar datos entre la base de datos y la interfaz.
