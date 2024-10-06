package com.mainhub.homebanking.models;

import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    private String CardHolder;
    private String cvv;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    @Enumerated(EnumType.STRING) //Enumerated indica que el tipo de dato es una cadena
    private CardType type;
    @Enumerated(EnumType.STRING) //Enumerated indica que el tipo de dato es una cadena
    private CardColor color;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Card() {
    }

    public Card(LocalDateTime expirationDate, CardType cardType, CardColor cardColor) {
    }

    public Card(CardType debit, CardColor gold, LocalDate now, LocalDate localDate) {
    }

    public Card(LocalDate thruDate, CardType type, CardColor color, Client luz) {

        this.thruDate = thruDate;
        this.type = type;
        this.color = color;

    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCardHolder() {
        return CardHolder;
    }

    public void setCardHolder(String cardHolder) {
        CardHolder = cardHolder;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", CardHolder='" + CardHolder + '\'' +
                ", cvv='" + cvv + '\'' +
                ", number='" + number + '\'' +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                ", type=" + type +
                ", color=" + color +
                '}';
    }

    // Método para generar un número de tarjeta de crédito completo
    public String generateCardNumber() {
        Random random = new Random(); // Crea una instancia de Random para generar números aleatorios
        StringBuilder cardNumber = new StringBuilder(); // Crea un StringBuilder para construir el número de tarjeta de crédito

        // Generar 16 dígitos en grupos de 4
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int digit = random.nextInt(10); // Genera un dígito aleatorio entre 0 y 9
                cardNumber.append(digit); // Agrega el dígito al StringBuilder
            }
            // Añadir un espacio después de cada grupo de 4 dígitos, excepto después del último
            if (i < 3) {
                cardNumber.append(" ");
            }
        }

        return cardNumber.toString(); // Devuelve el número de tarjeta de crédito generado como una cadena de texto
    }


    // Método para generar un CVV aleatorio
    public String generateCVV() {
        Random random = new Random();
        int cvv = random.nextInt(1000);

        // Completa el número con ceros si tiene solo una o dos cifras
        if (cvv < 10) {
            cvv = cvv * 100;
        } else if (cvv < 100) {
            cvv = cvv * 10;
        }

        return String.valueOf(cvv);
    }

//    // Método privado para calcular el dígito de verificación de un número de tarjeta de crédito
//    private int calcularDigitoVerificacion(String numeroTarjeta) {
//        int suma = 0; // Inicializa la suma a 0
//        int multiplicador = 1; // Inicializa el multiplicador a 1
//
//        // Recorre los dígitos del número de tarjeta de crédito en orden inverso
//        for (int i = numeroTarjeta.length() - 1; i >= 0; i--) {
//            int digito = Character.getNumericValue(numeroTarjeta.charAt(i)); // Convierte el dígito en un número entero
//            suma += digito * multiplicador; // Calcula la suma de los dígitos multiplicados por el multiplicador
//            multiplicador = (multiplicador == 1) ? 2 : 1; // Cambia el multiplicador a 2 si es 1, o a 1 si es 2
//        }
//
//        int digitoVerificacion = (suma * 9) % 10; // Calcula el dígito de verificación como el resto de la suma multiplicada por 9 dividida por 10
//        return (digitoVerificacion == 0) ? 0 : (10 - digitoVerificacion); // Devuelve el dígito de verificación o su complemento si es 0
//    }
}
