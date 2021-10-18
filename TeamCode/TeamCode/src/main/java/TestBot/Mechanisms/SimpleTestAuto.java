package TestBot.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "SimpleTestAuto")
public class SimpleTestAuto extends OpMode {
    enum State {
        START,
        MOVE,
        END
    }

    FreightBotInfo robot = new FreightBotInfo();
    State state = State.START;
    double lastTime;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void start() {
        state = SimpleTestAuto.State.START;
        resetStartTime();
        lastTime = getRuntime();
    }

    @Override
    public void loop() {
        telemetry.addData("State", state);
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Time in state", getRuntime() - lastTime);
       /* switch (state) {
            case START:
                if (getRuntime() >= 1.0) {
                    state = State.STRAFE;
                    lastTime = getRuntime();
                }
                break;
    }*/
    }
}