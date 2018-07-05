package com.patsnap.automation.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by liuyikai on 2017/9/6.
 */
public class ResourceUtil {
    
    
    public static File LoadResourceAsFile(Class c, String resourceName) throws URISyntaxException {
        
        URI uri = c.getResource(resourceName).toURI();
        File file = new File(uri);
        return  file;
    }
    
    
}
