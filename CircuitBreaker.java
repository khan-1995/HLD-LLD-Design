public class CircuitBreaker {
    private enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private int successCount = 0;
    private final int failureThreshold;
    private final int successThreshold;
    private final long retryTimePeriod;
    private long lastFailureTime;

    public CircuitBreaker(int failureThreshold, int successThreshold, long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.successThreshold = successThreshold;
        this.retryTimePeriod = retryTimePeriod;
    }

    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > retryTimePeriod) {
                state = State.HALF_OPEN;
            } else {
                return false;
            }
        }
        return true;
    }

    public synchronized void recordSuccess() {
        if (state == State.HALF_OPEN) {
            successCount++;
            if (successCount >= successThreshold) {
                reset();
            }
        } else {
            reset();
        }
    }

    public synchronized void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            lastFailureTime = System.currentTimeMillis();
        }
    }

    private void reset() {
        state = State.CLOSED;
        failureCount = 0;
        successCount = 0;
    }
}

/*Smaple class for testing*/
import java.util.Random;

public class Service {
    private final Random random = new Random();

    public void performAction() throws Exception {
        if (random.nextInt(10) < 3) { // 30% chance of failure
            throw new Exception("Service failure");
        }
        System.out.println("Service action performed successfully");
    }
}

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        CircuitBreaker circuitBreaker = new CircuitBreaker(3, 2, 5000); // Failure threshold: 3, Success threshold: 2, Retry period: 5000ms

        for (int i = 0; i < 20; i++) {
            try {
                if (circuitBreaker.allowRequest()) {
                    service.performAction();
                    circuitBreaker.recordSuccess();
                } else {
                    System.out.println("Request blocked by Circuit Breaker");
                }
            } catch (Exception e) {
                System.out.println("Service failed: " + e.getMessage());
                circuitBreaker.recordFailure();
            }

            try {
                Thread.sleep(500); // Wait for half a second before the next request
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/* Ouput for the problem
Service action performed successfully
Service action performed successfully
Service failed: Service failure
Service failed: Service failure
Service failed: Service failure
Request blocked by Circuit Breaker
Request blocked by Circuit Breaker
Request blocked by Circuit Breaker
Request blocked by Circuit Breaker
Request blocked by Circuit Breaker
Service action performed successfully
Service action performed successfully
Service action performed successfully
Service failed: Service failure
Service failed: Service failure
Service failed: Service failure
Request blocked by Circuit Breaker
Request blocked by Circuit Breaker
Service action performed successfully
Service action performed successfully

*/
