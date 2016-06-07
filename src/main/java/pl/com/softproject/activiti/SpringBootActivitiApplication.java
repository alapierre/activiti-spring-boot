package pl.com.softproject.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringBootActivitiApplication {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
	private RepositoryService repositoryService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private RuntimeService runtimeService;

	@SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
	private TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootActivitiApplication.class, args);
	}

    @Bean
    CommandLineRunner init() {

        return new CommandLineRunner() {

            public void run(String... strings) throws Exception {
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("jezyk_oryginalu", "pl");

                ProcessInstance process = runtimeService.startProcessInstanceByKey("manuskrypt", variables);

                step1();

                List<String> assigneeList = Arrays.asList(new String[]{"recenzent_jan", "recenzent_piotr", "recenzent_andrzej"});
                step2(assigneeList);

                step3(assigneeList);





            }
        };
    }

    private void step1() {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("redaktorzy").list();

        if(tasks.size() >= 1) {

            Task task = tasks.get(0);
            taskService.claim(task.getId(), "adrian");

            tasks = taskService.createTaskQuery().taskCandidateOrAssigned("adrian").list();

            System.out.println(tasks);

            taskService.complete(task.getId());


        } else {
            System.out.println("brak zadań!!!");
        }

    }

    private void step2(List<String> assigneeList) {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("redaktorzy").list();

        Map<String, Object> params = new HashMap<>();

        if(tasks.size() >= 1) {

            Task task = tasks.get(0);
            taskService.claim(task.getId(), "adrian");

            tasks = taskService.createTaskQuery().taskCandidateOrAssigned("adrian").list();

            System.out.println(tasks);

            params.put("assigneeList", assigneeList);

            taskService.complete(task.getId(), params);


        } else {
            System.out.println("brak zadań!!!");
        }

    }

    private void step3(List<String> assigneeList) {

        for (String assignee : assigneeList) {

            List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();

            for(Task task : tasks) {
                System.out.println( task.getName() + " -> " + task.getAssignee());
            }

            System.out.println(tasks);

            Task task = tasks.get(0);
            taskService.complete(task.getId());

        }


    }

}
