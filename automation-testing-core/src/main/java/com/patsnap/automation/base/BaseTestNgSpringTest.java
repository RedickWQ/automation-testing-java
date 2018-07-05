package com.patsnap.automation.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by liuyikai on 2017/9/5.
 */
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class BaseTestNgSpringTest extends AbstractTestNGSpringContextTests {

}
