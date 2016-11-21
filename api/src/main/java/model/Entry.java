package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Entry implements DataSerializable {

    private int age;
    private int literate;
    private int homeType;
    private String departmentName;
    private String provinceName;
    private int homeId;

    /*default*/ Entry() {

    }

    public Entry(int age, int literate, int homeType, String departmentName, String provinceName, int homeId) {
        this.age = age;
        this.literate = literate;
        this.homeType = homeType;
        this.departmentName = departmentName.trim();
        this.provinceName = provinceName.trim();
        this.homeId = homeId;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(getAge());
        objectDataOutput.writeInt(getLiterate());
        objectDataOutput.writeInt(getHomeType());
        objectDataOutput.writeUTF(getDepartmentName());
        objectDataOutput.writeUTF(getProvinceName());
        objectDataOutput.writeInt(getHomeId());
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        setAge(objectDataInput.readInt());
        setLiterate(objectDataInput.readInt());
        setHomeType(objectDataInput.readInt());
        setDepartmentName(objectDataInput.readUTF());
        setProvinceName(objectDataInput.readUTF());
        setHomeId(objectDataInput.readInt());
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLiterate() {
        return literate;
    }

    public void setLiterate(int literate) {
        this.literate = literate;
    }

    public int getHomeType() {
        return homeType;
    }

    public void setHomeType(int homeType) {
        this.homeType = homeType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

}
