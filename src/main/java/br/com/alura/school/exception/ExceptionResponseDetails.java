package br.com.alura.school.exception;

import lombok.Getter;

@Getter
public class ExceptionResponseDetails {
    private String title;
    private int status;
    private String detail;
    private String timestamp;
    private String message;

    private ExceptionResponseDetails() {
    }

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private String timestamp;
        private String message;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ExceptionResponseDetails build() {
            ExceptionResponseDetails exceptionResponseDetails = new ExceptionResponseDetails();
            exceptionResponseDetails.timestamp = this.timestamp;
            exceptionResponseDetails.detail = this.detail;
            exceptionResponseDetails.title = this.title;
            exceptionResponseDetails.message = this.message;
            exceptionResponseDetails.status = this.status;
            return exceptionResponseDetails;
        }
    }
}
