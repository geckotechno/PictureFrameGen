package com.geckotechno.pictureframe;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ImagePropertyMap extends HashMap implements Map {

    public ImagePropertyMap(Properties props) {
        init(props);
    }

    private void init(Properties props) {
        // parse a properties thay have values such as :
        //    1.Title=Car Hendge
        //    1.Name=004.JPG
        //    1.Date_created=1/3/2012 11:35 AM

        // convert #.name to key for this map collection
        for (Iterator it = (Iterator) props.propertyNames(); it.hasNext();) {
            Object obj = it.next();
            if (obj instanceof String) {
                String key = (String) obj;
                String[] strVals = key.split("\\.");
                if (strVals.length > 1) {
                    if ("Name".equals(strVals[1])) {
                        this.put(props.getProperty(key), new ValueObject(strVals[0]));
                    }
                }
            }
        }

        // loop through Value objects and get other attributes
        //    1.Title=Car Hendge
        //    1.Date_created=1/3/2012 11:35 AM
        for (Iterator it = this.entrySet().iterator(); it.hasNext();) {
            Map.Entry ent = (Map.Entry) it.next();
            ValueObject val = (ValueObject) ent.getValue();
            val.setTitle(props.getProperty(title(val.getKeyPrefix())));
            val.setCreateDate(props.getProperty(createDate(val.getKeyPrefix())));
        }

    }

    public String getTitle(String key) {
        String sReturn = null;
        ValueObject vObj = (ValueObject) this.get(key);
        if (vObj != null) {
            sReturn = vObj.getTitle();
        }
        return sReturn;
    }

    private String createDate(String keyPrefix) {
        return keyPrefix + ".Date_created";
    }

    private String title(String keyPrefix) {
        return keyPrefix + ".Title";
    }

    private class ValueObject {
        private String keyPrefix;
        private String title;
        private String createDate;

        private ValueObject(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        private String getTitle() {
            return title;
        }

        private void setTitle(String title) {
            this.title = title;
        }

        private String getCreateDate() {
            return createDate;
        }

        private void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        private String getKeyPrefix() {
            return keyPrefix;
        }
    }
}
