package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 1/17/18.
 */

public class ResponseBandeja {
    public String status;
    public boolean valid;
    public List<ResultBandeja> result;
    public int code;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<ResultBandeja> getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public class ResultBandeja
    {
        public String id_mensaje;
        public String mensaje;
        public String estado;
        public String fecha;
        public String imagen;

        private boolean ItemSelected=false;

        public boolean getItemSelected() {
            return ItemSelected;
        }

        public void setItemSelected(Boolean itemSelected) {
            ItemSelected = itemSelected;
        }

        public String getId_mensaje() {
            return id_mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }

        public String getEstado() {
            return estado;
        }

        public String getFecha() {
            return fecha;
        }

        public String getImagen() {
            return imagen;
        }
    }

}
