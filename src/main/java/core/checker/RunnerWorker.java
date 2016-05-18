package core.checker;

/**
 * Created by shybovycha on 18/05/16.
 */
public class RunnerWorker extends Thread {
    private final Process process;
    private Integer exit;

    public RunnerWorker(Process process) {
        this.process = process;
    }

    public void run() {
        try {
            exit = process.waitFor();
        } catch (InterruptedException ignore) {
            // TODO: logger
        }
    }

    public Integer getExit() {
        return exit;
    }
}
