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
      ```kotlin
         // Eliminar del dataSource inmediatamente
         if (dataSource.size == 1) {
             onEmptyCallback()
         }
         dataSource.removeAt(position)

         // Actualizar el adaptador inmediatamente
         adapter.notifyItemRemoved(position)

         // Crear y configurar la animación
         val animation = AnimationUtils.loadAnimation(rowView.context, android.R.anim.slide_out_right).apply {
             duration = 300
         }
         rowView.startAnimation(animation)
         // Esperar a que termine la animación antes de interactuar con el RecyclerView
         Handler(Looper.getMainLooper()).postDelayed({ afterDeleteCallBack() }, animation.duration)
      ```
    - **converters/DateConverters.kt**: Singleton para generar conversores de fechas LocalDate o LocalDateTime a String, siguiendo un regex específico
      ```kotlin
         private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
         private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss")

         @JvmStatic
         @TypeConverter
         fun fromString(value: String): LocalDate {
             return LocalDate.parse(value, dateFormatter)
         }

         @JvmStatic
         @TypeConverter
         fun toString(date: LocalDate): String {
             return date.format(dateFormatter)
         }

         fun toCustomString(date: LocalDate, dateFormatter: DateTimeFormatter): String {
             return date.format(dateFormatter)
         }

         @JvmStatic
         @TypeConverter
         fun toDateTimeString(dateTime: LocalDateTime): String {
             return dateTime.format(dateTimeFormatter)
         }
      ```
    - **decorators/**: Objetos que alteran la apariencia de elementos de la UI:
      - **RainbowTextViewDecorator.kt**: A la hora de realizar una búsqueda, resalta la parte coincidente del texto con un gradiente de arcoíris
        ```kotlin
        private val drawableResourceId: Int = R.drawable.rainbow_gradient

        override fun apply() {
            textView.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
                val gradientDrawable =
                    AppCompatResources.getDrawable(context, drawableResourceId) as GradientDrawable?
        
                var gradientColors = gradientDrawable!!.getColors()
                if (gradientColors == null) {
                    gradientColors =
                        intArrayOf(-0x10000, -0x100, -0xff0100, -0xff0001, -0xffff01, -0xff01)
                }

                val linearGradient = LinearGradient(
                    0f, 0f, textView.width.toFloat(), textView.textSize,
                    gradientColors,
                    null,
                    Shader.TileMode.CLAMP
                )
        
                textView.paint.setShader(linearGradient)
                textView.invalidate()
           })
        }
        ```
      - **SpacingItemDecoration.kt**: Añade una pequeña separación entre elementos de un RecyclerView
        ```kotlin
        class SpacingItemDecoration(private val context: Context) : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val vertical = context.getResources().getDimensionPixelSize(R.dimen.spacing_vertical)
                val horizontal = context.getResources().getDimensionPixelSize(R.dimen.spacing_horizontal)
                outRect.set(horizontal, vertical, horizontal, vertical)
            }
        }
        ```
    - **textWatchers/**: Aqui hay objetos que analizan constantemente los cambios de texto de un EditText con distintas finalizades
      - **actions/**: Acciones Runnable a aplicar por un TextWatcher sobre un elemento de la UI para alterar su estilo vía código Kotlin
        -**LongTextScrollerAction.kt**: Comprueba si el texto de la búsqueda no cabe en los límites de su respectivo TextView para desplazarlo hasta poder ver la coincidencia
        ```kotlin
        override fun invoke() {
            val lineWidth =
                 text.paint.measureText(text.text.toString())
            val viewWidth = text.width
            val endLine = text.layout.getLineForOffset(lastScroll.toInt() + viewWidth)
            // Obtener el índice del último carácter visible en la última línea visible
            val visibleText = text.layout.getOffsetForHorizontal(
                endLine.coerceAtMost(text.layout.lineCount - 1), // Validar línea máxima
                lastScroll + viewWidth                      // Última posición horizontal visible
            ).coerceAtMost(text.text.length)

            val endCharPosition = visibleText.coerceAtMost(text.text.length)
            val startCharPosition = visibleText.coerceAtLeast(0)
            val actualLastCharPosition =
                text.paint.measureText(text.text.toString(), 0, startIndex + busqueda.length - 1)
            if (lineWidth > viewWidth) {

                // Configuramos el TextView para manejar el desplazamiento
                text.setHorizontallyScrolling(true)
                text.isHorizontalScrollBarEnabled = false
                text.isSingleLine = true
                text.ellipsize = null // Desactivar truncamiento
                text.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START // Alineación desde el inicio

                // Creando y asignando Scroller
                val scroller = Scroller(text.context)
                text.setScroller(scroller)

                if (actualLastCharPosition < startCharPosition || actualLastCharPosition > endCharPosition) {
                    scroller.startScroll(lastScroll.toInt(), 0, (actualLastCharPosition -lastScroll).toInt(), 0)
                    lastScroll = text.x
                }
                text.invalidate()

           } else {
               text.isSelected = true
               text.isFocusable = true
               text.isFocusableInTouchMode = true
               text.ellipsize = TextUtils.TruncateAt.MARQUEE
               text.isHorizontalScrollBarEnabled = false
               text.setHorizontallyScrolling(false)
               text.scrollTo(0, 0)
           }
        }
        ```
      - **DateTextWatcher.kt**: Comprueba el valor introducido en el EditText asignado y delimita un valor mínimo (01/01/1970) y un valor máximo (LocalDate.now())
        ```kotlin
        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal: Calendar = Calendar.getInstance()

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {
                var clean = s.toString().replace("[^\\d]".toRegex(), "")
                val cleanC = current.replace("[^\\d]".toRegex(), "")

                val cl = clean.length
                var sel = cl
                var i = 2
                while (i <= cl && i < 6) {
                    sel++
                    i += 2
                }
                if (clean == cleanC) sel--

                if (clean.length < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length)
                } else {
                    var day = clean.substring(0, 2).toInt()
                    var mon = clean.substring(2, 4).toInt()
                    var year = clean.substring(4, 8).toInt()

                    year = max(1970.0, min(year.toDouble(), LocalDate.now().year.toDouble())).toInt()
                    if (year == LocalDate.now().year) {
                        mon = max(1.0, min(mon.toDouble(), LocalDate.now().monthValue.toDouble())).toInt()
                    } else {
                        mon = max(1.0, min(mon.toDouble(), 12.0)).toInt()
                    }
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, mon - 1)
                    if (mon == LocalDate.now().monthValue && year == LocalDate.now().year) {
                        day = max(1.0, min(day.toDouble(), LocalDate.now().dayOfMonth.toDouble())).toInt()
                    }else{
                        day = max(1.0, min(day.toDouble(), cal.getActualMaximum(Calendar.DAY_OF_MONTH).toDouble())).toInt()
                    }
                    clean = String.format(Locale.getDefault(), "%02d%02d%04d", day, mon, year)
               }
               clean = String.format(
                   "%s/%s/%s", clean.substring(0, 2),
                   clean.substring(2, 4),
                   clean.substring(4, 8)
               )

               current = clean
               editText.setText(current)
               editText.setSelection(min(sel.toDouble(), current.length.toDouble()).toInt())
            }
        }
        ```
      - **TextWatcherSearchBar%.kt**: Estos archivos asignan un comportamiento distinto a la barra de búsqueda según el resultado deseado
        ```kotlin
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           val busqueda = text.getText().toString().lowercase(
               Locale.getDefault()
           ).replace(" ", "")

           runBlocking {
               collect = db.contactoDAO()!!
                   .getContactos()
                   .map { c -> mapper.toCardItem(c) }
                   .toMutableList()
           }
           busqueda.ifEmpty {
               recyclerView!!.setAdapter(ContactoCardAdapter(collect, context!!))
               retriever.contador = 0
               LongTextScrollerAction.lastScroll = 0.0f
               return@onTextChanged
           }
           collect = collect.filter { c ->
               c.alias.replace(" ", "").lowercase(Locale.getDefault()).contains(busqueda)
           }.toMutableList()
           collect.ifEmpty {
               collect.add(ContactoCardItem.DEFAULT_FOR_NO_RESULTS)
               recyclerView!!.setAdapter(ContactoCardAdapter(collect, context!!))
               return@onTextChanged
           }
           val newAdapter = object : ContactoCardAdapter(collect, context!!) {
               override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
                   val cardItem = cardItemList[position]
                   holder.name.text = cardItem.nombre
                   holder.mostknownalias.text = SpannableString(cardItem.alias).apply {
                       cardItem.alias = cardItem.alias.let {
                           val spannable = SpannableString(it)
                           var startIndex = retriever.getStartIndex(busqueda, cardItem.alias)
                           if (startIndex >= 0) {
                               val shader = LinearGradient(
                                   0f, 0f, holder.mostknownalias.textSize * 2, 0f,
                                   intArrayOf(
                                       context!!.getColor(R.color.red),
                                       context.getColor(R.color.yellow),
                                       context.getColor(R.color.green),

                                       ),
                                   floatArrayOf(0f, 0.5f, 1f),
                                   Shader.TileMode.MIRROR
                               )

                               setSpan(
                                   ForegroundShaderSpan(shader),
                                   startIndex,
                                   retriever.getSpanIntervalJump(busqueda, cardItem.alias, startIndex),
                                   Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                               )

                               holder.mostknownalias.post(LongTextScrollerAction(holder.mostknownalias, startIndex, busqueda))
                           }
                           spannable.toString()
                       }

                   }
                   if (cardItem.idAnotable != -1) {
                       holder.careto.setImageResource(R.drawable.contact_icon)
                       holder.careto.setColorFilter(
                           cardItem.colorFoto,
                           PorterDuff.Mode.MULTIPLY
                       )
                   } else {
                       holder.careto.setImageResource(R.drawable.notfound)
                   }
                   if (cardItem.clickable) {
                       holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
                           val intent = Intent(context, ContactoActivity::class.java)
                           intent.putExtra("id", cardItem.idAnotable)
                           context!!.startActivity(intent)
                       })
                  }
               }
           }
           recyclerView!!.setLayoutManager(LinearLayoutManager(context))
           recyclerView.setAdapter(newAdapter)
        }
    - **ApplicationUpdater.kt**: Singleton que genera la notificación de descarga y la petición de permisos de instalación de orígenes desconocidos
      ```kotlin
      fun downloadAndInstallAPK(
          context: Context,
          url: String,
          fileName: String,
          unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
      ) {
          if (!context.packageManager.canRequestPackageInstalls()) {
              handleUnknownAppSourcesPermission(context, unknownAppsPermissionLauncher)
              return
          }

          Log.d("GithubController", "URI de descarga: ${url.toUri()}")
          val request = DownloadManager.Request(url.toUri())
              .setTitle("Actualizacion de SpyLook")
              .setDescription("Espere mientras se descarga la actualización.")
              .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
              .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
              .setAllowedNetworkTypes(
                  DownloadManager.Request.NETWORK_WIFI or
                          DownloadManager.Request.NETWORK_MOBILE
              )
              .setAllowedOverMetered(true)
              .setAllowedOverRoaming(true)

          val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
          downloadId = downloadManager.enqueue(request)
          Log.d("GithubController", "Solicitud de descarga realizada. ID de descarga: $downloadId")
       }

       private fun handleUnknownAppSourcesPermission(
           context: Context,
           unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
       ) {
           val intent = Intent(
               Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
               ("package:" + context.packageName).toUri()
           )
           Toast.makeText(context, UNKNOWN_APPS_PERMISSION_MSG, Toast.LENGTH_LONG).show()
           unknownAppsPermissionLauncher.launch(intent)

      }
      ```
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
