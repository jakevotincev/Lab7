import java.io.Serializable;

class Response implements Serializable {
    private static final long serialVersionUID = 3;
    private Object response;
    private Status status=Status.NOSTATUS;

    public void setResponse(Object response) {
        this.response = response;
    }

    public Status getStatus() {
        return status;
    }

    Response(Object response) {
        this.response = response;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public Response(){

    }

    Object getResponse() {
        return response;
    }
}
