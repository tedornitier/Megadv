package app.alessandrotedd.megadv.report

import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.dataModel.ReportModel

class ReportActionItem(entry: ReportModel, val time: String) : ReportItem() {

    var iconResource: Int
    var iconBackgroundResource: Int
    var action: String

    init {
        // set crediti
        val crediti = entry.crediti.toInt()
        // set object
        val obj = entry.obj
        // set icon, action name and icon background depending on the action ID
        when (entry.action) {
            1, 2 -> {
                iconResource = R.drawable.rocket
                action = "Hai speso $crediti crediti per il tuo upgrade"
                iconBackgroundResource = R.drawable.yellow_circle
            }
            3 -> {
                iconResource = R.drawable.cursor
                action = "Hai guadagnato $crediti crediti per aver ottenuto un click sul tuo circuito di scambio banner"
                iconBackgroundResource = R.drawable.green_circle
            }
            4 -> {
                iconResource = R.drawable.cursor
                action = "Hai speso $crediti crediti per aver ottenuto un click sul tuo banner pubblicitario"
                iconBackgroundResource = R.drawable.yellow_circle
            }
            5 -> {
                iconResource = R.drawable.thumbsup
                action = "Hai guadagnato $crediti crediti per aver messo \"Mi piace\" ad una pagina Facebook"
                iconBackgroundResource = R.drawable.green_circle
            }
            6 -> {
                iconResource = R.drawable.thumbsdown
                action = "Hai rimborsato $crediti crediti per aver tolto un \"Mi piace\" alla pagina https://www.facebook.com/$obj"
                iconBackgroundResource = R.drawable.red_circle
            }
            7 -> {
                iconResource = R.drawable.thumbsdown
                action = "Hai ottenuto un rimborso di $crediti crediti perché è stato levato uno o più 'Mi piace' dalla tua pagina Facebook https://www.facebook.com/$obj"
                iconBackgroundResource = R.drawable.green_circle
            }
            8 -> {
                iconResource = R.drawable.thumbsup
                action = "Hai speso $crediti crediti per aver ottenuto un \"Mi piace\" sulla tua pagina Facebook https://www.facebook.com/$obj"
                iconBackgroundResource = R.drawable.yellow_circle
            }
            9 -> {
                iconResource = R.drawable.bag
                action = "Hai acquistato $crediti crediti a $obj€"
                iconBackgroundResource = R.drawable.blue_circle
            }
            10 -> {
                iconResource = R.drawable.key
                action = "Hai guadagnato $crediti crediti per aver effettuato l'accesso quotidiano a Megadv"
                iconBackgroundResource = R.drawable.green_circle
            }
            11 -> {
                iconResource = R.drawable.key
                action = "Hai effettuato l'accesso a Megadv"
                iconBackgroundResource = R.drawable.gray_circle
            }
            12 -> {
                iconResource = R.drawable.key
                action = "Hai disconnesso il tuo account Megadv"
                iconBackgroundResource = R.drawable.gray_circle
            }
            13 -> {
                iconResource = R.drawable.ticket
                action = "Hai speso $crediti crediti per l'acquisto di un ticket. Ticket valido per l'estrazione $obj"
                iconBackgroundResource = R.drawable.yellow_circle
            }
            14 -> {
                iconResource = R.drawable.users
                action = "Gli utenti a cui hai consigliato Megadv ti hanno permesso di guadagnare $crediti crediti"
                iconBackgroundResource = R.drawable.green_circle
            }
            15 -> {
                iconResource = R.drawable.trophy
                action = "Hai guadagnato $crediti crediti per aver vinto l'intero Jackpot in palio. Partecipa alla prossima estrazione settimanale"
                iconBackgroundResource = R.drawable.green_circle
            }
            16 -> {
                iconResource = R.drawable.settings
                action = "Hai aggiornato le tue preferenze"
                iconBackgroundResource = R.drawable.gray_circle
            }
            17 -> {
                iconResource = R.drawable.chat
                action = "Hai inserito un nuovo banner nel circuito pubblicitario"
                iconBackgroundResource = R.drawable.green_circle
            }
            18 -> {
                iconResource = R.drawable.facebook
                action = "Hai inserito una nuova pagina Facebook nel circuito di scambio 'Mi piace'"
                iconBackgroundResource = R.drawable.green_circle
            }
            19 -> {
                iconResource = R.drawable.chat
                action = "Hai modificato il tuo banner nel circuito pubblicitario"
                iconBackgroundResource = R.drawable.gray_circle
            }
            20 -> {
                iconResource = R.drawable.facebook
                action = "Hai modificato la tua pagina Facebook nel circuito di scambio 'Mi piace'"
                iconBackgroundResource = R.drawable.gray_circle
            }
            21 -> {
                iconResource = R.drawable.chat
                action = "Hai eliminato il tuo banner dal circuito pubblicitario"
                iconBackgroundResource = R.drawable.red_circle
            }
            22 -> {
                iconResource = R.drawable.facebook
                action = "Hai eliminato la tua pagina Facebook dall circuito di scambio 'Mi piace'"
                iconBackgroundResource = R.drawable.red_circle
            }
            23 -> {
                iconResource = R.drawable.flag
                action = "Hai contattato il centro assistenza"
                iconBackgroundResource = R.drawable.gray_circle
            }
            25 -> {
                iconResource = R.drawable.key
                action = "Hai creato il tuo account Megadv"
                iconBackgroundResource = R.drawable.green_circle
            }
            else -> {
                iconResource = R.drawable.flag
                action = "Errore: azione non riconosciuta. Aggiorna l'applicazione o contatta gli sviluppatori su megadvert@gmail.com"
                iconBackgroundResource = R.drawable.green_circle
            }
        }
    }
}