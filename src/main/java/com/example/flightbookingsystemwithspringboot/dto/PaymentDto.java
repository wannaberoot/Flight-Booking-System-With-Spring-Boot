package com.example.flightbookingsystemwithspringboot.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@JsonTypeName("payment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Data
public class PaymentDto implements Serializable {

    private static final long serialVersionUID = -9131952628646484154L;

    @NotNull
    @NotEmpty
    private List<String> seatNumbers;
    @NotNull
    @NotEmpty
    private String cardHolderName;
    @NotNull
    @NotEmpty
    private String cardNumber;
    @NotNull
    @NotEmpty
    private String cardExpireMonth;
    @NotNull
    @NotEmpty
    private String cardExpireYear;
    @NotNull
    @NotEmpty
    private String cardCvc;

    public String toString() {
        return "PaymentDto{" +
                "seatNumbers=" + seatNumbers +
                ", cardHolderName=" + cardHolderName +
                ", cardNumber=" + cardNumber +
                ", cardExpireMonth=" + cardExpireMonth +
                ", cardExpireYear=" + cardExpireYear +
                ", cardCvc=" + cardCvc +
                '}';
    }
}
