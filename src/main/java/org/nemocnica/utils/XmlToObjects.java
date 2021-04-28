package org.nemocnica.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.*;

//pure utility
//reads map from xml file from resources
public class XmlToObjects {
    private static final Logger logger = LoggerFactory.getLogger(XmlToObjects.class.getName());

    private XmlToObjects() {
    }

    public static Map<String, String> getMap(String resourceName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MapWrapper.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            MapWrapper wrapper = (MapWrapper) jaxbUnmarshaller.unmarshal(XmlToObjects.class.getResourceAsStream(resourceName));
            return wrapper.getMap();
        } catch (JAXBException e) {
            logger.error("Error reading map from resource:{}", resourceName, e);
        }
        return Collections.emptyMap();
    }

    public static List<Entry> getList(String resourceName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ListWrapper.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ListWrapper wrapper = (ListWrapper) jaxbUnmarshaller.unmarshal(XmlToObjects.class.getResourceAsStream(resourceName));
            return wrapper.getList();
        } catch (JAXBException e) {
            logger.error("Error reading list from resource:{}", resourceName, e);
        }
        return Collections.emptyList();
    }


    @XmlRootElement(name = "MapWrapper")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MapWrapper {
        private Map<String, String> map = new HashMap<>();

        public MapWrapper() {
        }

        public MapWrapper(Map<String, String> map) {
            this.map = map;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

    @XmlRootElement(name = "ListWrapper")
    @XmlSeeAlso({Entry.class})
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ListWrapper {
        @XmlElement(name = "entry")
        private List<Entry> list = new ArrayList<>();

        public ListWrapper() {
        }

        public List<Entry> getList() {
            return list;
        }

        public void setList(List<Entry> list) {
            this.list = list;
        }

        public ListWrapper(List<Entry> list) {
            this.list = list;
        }

    }

    public static void main(String args[]) throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(ListWrapper.class);
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//        ListWrapper listWrapper = new ListWrapper(Arrays.asList(new Entry("a","v a"),new Entry("b"," v b")));
//        //Marshal the employees list in console
//        jaxbMarshaller.marshal(listWrapper, System.out);
     List<Entry> list = XmlToObjects.getList("/sql/create.xml");
        System.out.println(list);
    }


}
