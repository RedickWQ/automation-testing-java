package com.patsnap.automation.context;

import com.patsnap.automation.gui.framework.GuiTestContext;
import com.patsnap.automation.report.FakeReporter;
import com.patsnap.automation.report.MemoryReporter;

/**
 *
 * @author liuyikai
 * @date 2017/9/7
 */
public class TestContextManager {

    public static ThreadLocal<TestContext> contexts = new ThreadLocal<TestContext>();
    
    
    public static void setContext(TestContext context){
        contexts.set(context);
    }
    
    public static TestContext getContext(){
        TestContext context =  contexts.get();
        
        if (context == null) {
            context = new TestContext();
            //todo enable multi reporter
            context.setReporter(new FakeReporter());
        }
    
        contexts.set(context);
        return  context;
    }
    
    
   
    public static void remove(){
        TestContext context =  contexts.get();
        
//        if (context != null && context instanceof GuiTestContext) {
//            ( (GuiTestContext) context).terminateDriver();
//        }
        context.endContext();
        contexts.remove();
    }
    
    
}
