public class MySimpleThread extends Thread {
    private String fileName;

    public MySimpleThread(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        Sound.playSound(fileName).join();
    }
}
