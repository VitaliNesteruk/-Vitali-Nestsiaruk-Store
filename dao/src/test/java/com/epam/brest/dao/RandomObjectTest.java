package com.epam.brest.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RandomObjectTest {

    @Test
    public void testData() throws IOException, ClassNotFoundException {
        String filename = "objects.data";

        RandomObject ro1 = new RandomObject();
        RandomObject ro2 = new RandomObject();

        System.out.println("write");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(ro1);
            out.writeObject(ro2);
        }

        RandomObject result1;
        RandomObject result2;

        System.out.println("read");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            result1 = (RandomObject) in.readObject();
            result2 = (RandomObject) in.readObject();
        }
        Assertions.assertArrayEquals(ro1.data, result1.data);
        Assertions.assertArrayEquals(ro2.data, result2.data);
    }

    @Test
    public void testXml() throws IOException, ClassNotFoundException {
        String filename = "objects.xml";

        RandomObject ro1 = new RandomObject();
        RandomObject ro2 = new RandomObject();

        System.out.println("write");
        try (XMLEncoder out = new XMLEncoder(new FileOutputStream(filename))) {
            out.writeObject(ro1);
            out.writeObject(ro2);
        }

        RandomObject result1;
        RandomObject result2;

        System.out.println("read");
        try (XMLDecoder in = new XMLDecoder(new FileInputStream(filename))) {
            result1 = (RandomObject) in.readObject();
            result2 = (RandomObject) in.readObject();
        }
        Assertions.assertArrayEquals(ro1.data, result1.data);
        Assertions.assertArrayEquals(ro2.data, result2.data);
    }

    @Test
    public void testJson() throws IOException{
        String filename = "objects.json";

        RandomObject ro1 = new RandomObject();
        RandomObject ro2 = new RandomObject();
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("write");
        try (OutputStream out = new FileOutputStream(filename)) {
            mapper.writeValue(out, Arrays.asList(ro1,ro2));
        }

        List<RandomObject> result;

        System.out.println("read");
        try (InputStream in = new FileInputStream(filename)) {
            result = mapper.readerForListOf(RandomObject.class).readValue(in);
        }
        Assertions.assertArrayEquals(ro1.data, result.get(0).data);
        Assertions.assertArrayEquals(ro2.data, result.get(1).data);
    }
}
