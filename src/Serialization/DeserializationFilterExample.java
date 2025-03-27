package Serialization;

import java.io.*;
import java.util.ArrayList;

class Sample implements Externalizable{
    private String data1;
    private String data2;
    private transient  String data3;

    public Sample() {
    }

    public Sample(String data1, String data2, String data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(data1);
        out.writeObject(data2);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.data1 = (String) in.readObject();
    this.data2 = (String) in.readObject();
    }
}

public class DeserializationFilterExample {
    public static void main(String[] args) throws IOException {

   Sample sample = new Sample("data1","data2","data3");
     FileOutputStream fileOutputStream=new FileOutputStream("data.ser");
     ObjectOutputStream oos=new ObjectOutputStream(fileOutputStream);
        try {
            oos.writeObject(sample);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileInputStream fis=new FileInputStream("data.ser");
        //Method1: Apply Per-Stream Filters
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.setObjectInputFilter(info -> {
            String className = info.serialClass().getName();
            if ("com.example.MyClass".equals(className) ||
                    "java.util.ArrayList".equals(className)) {
                return ObjectInputFilter.Status.ALLOWED;
            }
            return ObjectInputFilter.Status.REJECTED;
        });



             //Method 2: Use ObjectInputFilter

        try (FileInputStream fis1 = new FileInputStream("data.ser");
             ObjectInputStream ois1 = new ObjectInputStream(fis1)) {

            // Set the serialization filter
            ObjectInputFilter filter = ObjectInputFilter.Config.createFilter("!*");
            ObjectInputFilter.Config.setSerialFilter(filter);

            // Deserialize the object
            Object obj = ois1.readObject();
            System.out.println("Deserialized object: " + obj);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}