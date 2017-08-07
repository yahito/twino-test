package twino.test.loan.dto;

/**
 * Created by poryadin.andrey on 06/08/2017.
 */
public enum ResultCode {
    SUCCESS(0),
    TOO_MANY_REQUESTS(-1),
    BLOCKED(-2),
    OTHER(-3);

    final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
