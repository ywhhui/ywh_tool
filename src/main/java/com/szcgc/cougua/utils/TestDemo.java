//package utils;
//
//
//public class TestDemo {
//
//    public static class ThreadA extends Thread {
//
//        public ThreadA(TestVo service) {
//            super();
//        }
//        int i =0 ;
//
//        public synchronized void  run(TestVo service) {
//            while (service.isCount()){
//                System.out.println("i----"+i);
//                i++;
//                if(i>33){
//                    break;
//                }
//            }
//        }
//
//    }
//
//    public static class ThreadB extends Thread {
//
//        public ThreadB(TestVo service) {
//            super();
//        }
//
//        public synchronized void run(TestVo service) {
//            service.init(false);
//            System.out.println("it111----"+service.isCount());
//        }
//
//    }
//    public static void main(String[] args) {
//        TestVo service = new TestVo();
//        service.init(true);
//
//        ThreadA athread = new ThreadA(service);
//        athread.start();
//        athread.run(service);
//
//        ThreadB bthread = new ThreadB(service);
//        bthread.start();
//        bthread.run(service);
//    }
//}
//
