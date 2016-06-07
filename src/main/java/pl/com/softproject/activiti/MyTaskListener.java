/**
 * Copyright 2016-06-07 the original author or authors.
 */
package pl.com.softproject.activiti;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

/**
 * @author Adrian Lapierre {@literal <adrian@soft-project.pl>}
 */

@Service("myTaskListener")
public class MyTaskListener {

    public void perform(Task task) {

        System.out.println(">>>>>> in listener " + task);
        System.out.println("przypisany do:" + task.getAssignee());

    }


}



