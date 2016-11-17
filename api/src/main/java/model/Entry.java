package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Entry implements DataSerializable {

    private int edad;
    private String analfabetismo;
    private String tipoDeVivienda;
    private String nombreDepartamento;
    private String nombreProvincia;
    private String hogarId;

    /*default*/ Entry() {

    }

    public Entry(int edad, String analfabetismo, String tipoDeVivienda, String nombreDepartamento, String nombreProvincia, String hogarId) {
        this.edad = edad;
        this.analfabetismo = analfabetismo;
        this.tipoDeVivienda = tipoDeVivienda;
        this.nombreDepartamento = nombreDepartamento;
        this.nombreProvincia = nombreProvincia;
        this.hogarId = hogarId;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(edad);
        objectDataOutput.writeUTF(analfabetismo);
        objectDataOutput.writeUTF(tipoDeVivienda);
        objectDataOutput.writeUTF(nombreDepartamento);
        objectDataOutput.writeUTF(nombreProvincia);
        objectDataOutput.writeUTF(hogarId);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {

    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getAnalfabetismo() {
        return analfabetismo;
    }

    public void setAnalfabetismo(String analfabetismo) {
        this.analfabetismo = analfabetismo;
    }

    public String getTipoDeVivienda() {
        return tipoDeVivienda;
    }

    public void setTipoDeVivienda(String tipoDeVivienda) {
        this.tipoDeVivienda = tipoDeVivienda;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public String getHogarId() {
        return hogarId;
    }

    public void setHogarId(String hogarId) {
        this.hogarId = hogarId;
    }

}
