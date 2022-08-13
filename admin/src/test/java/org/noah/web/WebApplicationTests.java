package org.noah.web;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment= RANDOM_PORT)
class WebApplicationTests {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Test
    void contextLoads() {
    }

    String staffId = "1000";
    /**
     * 开启一个流程
     */
    @Test
    void askForLeave() {
        Map<String, Object> map = new HashMap<>();
        map.put("leaveTask", staffId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave", map);
        runtimeService.setVariable(processInstance.getId(), "name", "javaboy");
        runtimeService.setVariable(processInstance.getId(), "reason", "休息一下");
        runtimeService.setVariable(processInstance.getId(), "days", 10);
//        runtimeService.getVariables(processInstance.getId());
        System.out.println("创建请假流程 processId：" + processInstance.getId());
        List<Task> list = taskService.createTaskQuery().taskAssignee(staffId).orderByTaskId().desc().list();
//        list.get(0).getProcessInstanceId()
        //a45e0600-09d6-11ed-a70e-701ab81c148e
    }

    String zuzhangId = "90";
    String managerId = "01";
    /**
     * 提交给组长审批
     */
    @Test
    void submitToZuzhang() {
        //员工查找到自己的任务，然后提交给组长审批
        List<Task> list = taskService.createTaskQuery().taskAssignee(staffId).orderByTaskId().desc().list();
        for (Task task : list) {
            System.out.printf("任务 ID：%s；任务处理人：%s；任务是否挂起：%s%n", task.getId(), task.getAssignee(), task.isSuspended());
            Map<String, Object> map = new HashMap<>();
            //提交给组长的时候，需要指定组长的 id
            map.put("zuzhangTask", zuzhangId);
            taskService.complete(task.getId(), map);
        }
    }



    /**
     * 组长审批-批准
     */
    @Test
    void zuZhangApprove() {
        List<Task> list = taskService.createTaskQuery().taskAssignee(zuzhangId).orderByTaskId().desc().list();
        for (Task task : list) {
            System.out.printf("组长 %s 在审批 %s 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            //组长审批的时候，如果是同意，需要指定经理的 id
            map.put("managerTask", managerId);
            map.put("checkResult", "通过");
            taskService.complete(task.getId(), map);
        }
    }

    /**
     * 组长审批-拒绝
     */
    @Test
    void zuZhangReject() {
        List<Task> list = taskService.createTaskQuery().taskAssignee(zuzhangId).orderByTaskId().desc().list();
        for (Task task : list) {
            System.out.printf("组长 %s 在审批 %s 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            //组长审批的时候，如果是拒绝，就不需要指定经理的 id
            map.put("checkResult", "拒绝");
            taskService.complete(task.getId(), map);
        }
    }

    /**
     * 经理审批自己的任务-批准
     */
    @Test
    void managerApprove() {
        List<Task> list = taskService.createTaskQuery().taskAssignee(managerId).orderByTaskId().desc().list();
        for (Task task : list) {
            System.out.printf("经理 %s 在审批 %s 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("checkResult", "通过");
            taskService.complete(task.getId(), map);
        }
    }

    /**
     * 经理审批自己的任务-拒绝
     */
    @Test
    void managerReject() {
        List<Task> list = taskService.createTaskQuery().taskAssignee(managerId).orderByTaskId().desc().list();
        for (Task task : list) {
            System.out.printf("经理 %s 在审批 %s 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("checkResult", "拒绝");
            taskService.complete(task.getId(), map);
        }
    }

}
