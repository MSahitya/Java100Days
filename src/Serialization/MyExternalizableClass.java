package Serialization;

import java.io.*;

public class MyExternalizableClass implements Externalizable {

    private String data1;
    private int data2;
    private transient String data3; // Will not be serialized

    // Public no-argument constructor (required)
    public MyExternalizableClass() {
        System.out.println("No-argument constructor called");
    }

    public MyExternalizableClass(String data1, int data2, String data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    // Implementation of writeExternal() method
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(data1); // Use writeObject for Strings
        out.writeInt(data2);       // Use writeInt for ints
        // data3 is transient, so we don't write it
    }

    // Implementation of readExternal() method
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        data1 = (String) in.readObject(); // Read in the same order as written
        data2 = in.readInt();
        // data3 is transient, so it will be null after deserialization
        System.out.println("readExternal called");
    }

    public String getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    @Override
    public String toString() {
        return "MyExternalizableClass{" +
                "data1='" + data1 + '\'' +
                ", data2=" + data2 +
                ", data3='" + data3 + '\'' +
                "}";
    }

    public static void main(String[] args) {
        String originalData3 = "Original Data 3";
        MyExternalizableClass obj = new MyExternalizableClass("Hello", 123, originalData3);
        String fileName = "myExternalizable.dat";

        // Serialize the object
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);
            System.out.println("Object serialized: " + obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the object
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            MyExternalizableClass deserializedObj = (MyExternalizableClass) ois.readObject();
            System.out.println("Object deserialized: " + deserializedObj);
            System.out.println("deserializedObj.getData3() : " + deserializedObj.getData3());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

