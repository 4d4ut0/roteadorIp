package br.com.neto.adauto.exception;

public class ApplicationException extends Exception {

    private String[] params;

  
    public ApplicationException() {
        super();
    }

    public ApplicationException(String msg, Throwable arg1) {
        super(msg, arg1);
    }

    public ApplicationException(String... params) {
        super();
        this.params = params;
    }

    public ApplicationException(String msg, Throwable arg1, String... params) {
        super(msg, arg1);
        this.params = params;
    }

    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(Throwable msg) {
        super(msg);
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String... params) {
        this.params = params;
    }

}

