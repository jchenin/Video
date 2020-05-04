package jin.chen.enums;

public enum VideoStatusEnum {
    Success(1),//1  ->>  正常
    Failure(-1);//-1  ->> 需要禁止播放

    public  final  int value;

    VideoStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
