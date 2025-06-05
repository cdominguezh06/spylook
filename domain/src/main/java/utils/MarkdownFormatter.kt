package utils

object MarkdownFormatter {
    operator fun invoke(markdown: String): String {
        val cleanMarkdown = markdown
            .replace(Regex("^>\\s*", RegexOption.MULTILINE), "") // Elimina "> " al inicio de líneas
            .replace(Regex("\\[!Warning\\]", RegexOption.IGNORE_CASE), "⚠️ Precaución")
            .replace(Regex("\\[!Note\\]", RegexOption.IGNORE_CASE), "💡 Notas")

        return cleanMarkdown
    }
}