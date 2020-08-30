package ru.sber.task3;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

//3
//Есть отделение почты номер 1. Оно принимает и отправляет посылки.
// На почте два оператора. Один принимает посылки - за раз он может принять от 1 до 3 посылок от одного человека.
// Второй отправляет посылки - за раз может отправить от 1 до 5 посылок.
// Время на операцию для принимающего - 500мс, для отправляющего - 600мс.
// После каждых 20 операций принимающий уходит на обед на 5с.
// Отправляющий после 25 операций уходит на обед на 6с. Потом все повторяется.
// Изначально на почте 50 посылок.
//
// Есть отделение почты номер 2. Здесь все аналогично, только цифры другие.
// Принять за раз может от 1 до 2 посылок от одного человека. Отправить за раз может от 1 до 4 посылок.
// Время на операцию отправки - 450мс, приема - 550мс. После каждых 23 операций приема оператор уходит на обед на 6с.
// После каждых 27 операций отправки оператор уходит на обед на 5с.
// Изначально на почте 55 посылок.
//
//Тот, кто первым отправит все посылки со своего отделения, тот получает премию от почты России.
// Если же число посылок превысит 100, то операторов этой почты уволят за плохую медленную работу.
//
//Поэтому операторы из этих двух отделений почты конкуренты друг другу. И ради заветного приза готовы на разные гадости.
//А именно - во время обеда операторы 1 почты могут взять от 0 до 5 посылок со своей почты и подбросить конкуренту. Для 2 почты - от 0 до 7 посылок.


//Если принимающий 1 почты видит, что у него уже больше 80 посылок и увольнение не за горами,
// то он увеличивает свой обед до 6с, чтобы меньше посылок принять от людей.
//Если же у него посылок менее 20, то он почти уверен в своей победе и уменьшает обед до 4с.
//Отправляющий 1 почты при количестве посылок больше 75 уменьшает свой обед до 5с, чтобы больше посылок отправить.
//А если посылок осталось менее 25, то увеличивает до 7с.
//
//Для 2 почты принимающий при более 79 посылок увеличивает обед до 6,5с, при менее 19 посылок уменьшает до 4с.
//Для 2 почты отправляющий при более 72 посылок уменьшает обед до 4,5с, при менее 23 посылок увеличивает до 6,5с.
//Если спустя 2 минуты состязание почтальонов не окончилось, то приз получает тот, кто отправил больше всего посылок.
//
//Нужно смоделировать этот процесс в реальном времени. Вывести сведения, какая операция сейчас проводится в каком отделении почты (или у них обед),
//сколько всего посылок на почте, кто кому что подбросил, кто увеличивает или уменьшает длительность своего обеда. В общем, все выводить подробно.
//
//PS Не смотря на большой размер задачи и обилие цифр, она намного проще, чем может показаться при первом прочтении.
public class Post {
    private static final Logger logger = Logger.getLogger("Post");
    private String namePost;
    private volatile Boolean winLoose = null;
    private Post anotherPost;
    private volatile AtomicInteger numberParcels;
    private final int looseNumberParcels = 100;
    private final int winNumberParcels = 0;
    private final int timeForFinish = 120_000;
    private int countOfOperationBeforeSupperReceiver;
    private int countOfOperationBeforeSupperSender;
    private int numberMaxRandomParcelsReceiver;
    private int numberMaxRandomParcelsSender;
    private int delayReceiver;
    private int delaySender;
    private volatile int delaySupperReceiver;
    private volatile int delaySupperSender;
    private int numberSendParcelsToAnotherPost;
    private int numberIncreaseSupperReceiver;
    private int numberIncreaseSupperSender;
    private int delayIncreaseSupperReceiver;
    private int delayIncreaseSupperSender;
    private int numberDecreaseSupperReceiver;
    private int numberDecreaseSupperSender;
    private int delayDecreaseSupperReceiver;
    private int delayDecreaseSupperSender;

    public Post(String namePost, AtomicInteger numberParcels,
                int countOfOperationBeforeSupperReceiver,
                int countOfOperationBeforeSupperSender,
                int numberMaxRandomParcelsReceiver,
                int numberMaxRandomParcelsSender,
                int delayReceiver,
                int delaySender,
                int delaySupperReceiver,
                int delaySupperSender,
                int numberSendParcelsToAnotherPost,
                int numberIncreaseSupperReceiver,
                int numberIncreaseSupperSender,
                int delayIncreaseSupperReceiver,
                int delayIncreaseSupperSender,
                int numberDecreaseSupperReceiver,
                int numberDecreaseSupperSender,
                int delayDecreaseSupperReceiver,
                int delayDecreaseSupperSender) {
        this.namePost = namePost;
        this.numberParcels = numberParcels;
        this.countOfOperationBeforeSupperReceiver = countOfOperationBeforeSupperReceiver;
        this.countOfOperationBeforeSupperSender = countOfOperationBeforeSupperSender;
        this.numberMaxRandomParcelsReceiver = numberMaxRandomParcelsReceiver;
        this.numberMaxRandomParcelsSender = numberMaxRandomParcelsSender;
        this.delayReceiver = delayReceiver;
        this.delaySender = delaySender;
        this.delaySupperReceiver = delaySupperReceiver;
        this.delaySupperSender = delaySupperSender;
        this.numberSendParcelsToAnotherPost = numberSendParcelsToAnotherPost;
        this.numberIncreaseSupperReceiver = numberIncreaseSupperReceiver;
        this.numberIncreaseSupperSender = numberIncreaseSupperSender;
        this.delayIncreaseSupperReceiver = delayIncreaseSupperReceiver;
        this.delayIncreaseSupperSender = delayIncreaseSupperSender;
        this.numberDecreaseSupperReceiver = numberDecreaseSupperReceiver;
        this.numberDecreaseSupperSender = numberDecreaseSupperSender;
        this.delayDecreaseSupperReceiver = delayDecreaseSupperReceiver;
        this.delayDecreaseSupperSender = delayDecreaseSupperSender;
    }

    //Есть отделение почты номер 1. Оно принимает и отправляет посылки.
    // На почте два оператора. Один принимает посылки - за раз он может принять от 1 до 3 посылок от одного человека.
    // Второй отправляет посылки - за раз может отправить от 1 до 5 посылок.
    // Время на операцию для принимающего - 500мс, для отправляющего - 600мс.
    // После каждых 20 операций принимающий уходит на обед на 5с.
    // Отправляющий после 25 операций уходит на обед на 6с. Потом все повторяется.
    // Изначально на почте 50 посылок.

    //Поэтому операторы из этих двух отделений почты конкуренты друг другу. И ради заветного приза готовы на разные гадости.
//А именно - во время обеда операторы 1 почты могут взять от 0 до 5 посылок со своей почты и подбросить конкуренту. Для 2 почты - от 0 до 7 посылок.
    private Runnable runnableReceiver =
            () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        for (int i = 0; i < countOfOperationBeforeSupperReceiver; i++) {
                            int numberParcelsReceived = new Random().nextInt(numberMaxRandomParcelsReceiver) + 1;
                            numberParcels.addAndGet(numberParcelsReceived);
                            logger.info(namePost + " received: " + numberParcelsReceived);
                            logger.info(namePost + " numberParcels: " + numberParcels);
                            Thread.sleep(delayReceiver);
                        }
                        int numberSendToAnotherPostRandom = new Random().nextInt(numberSendParcelsToAnotherPost);
                        numberParcels.addAndGet(-numberSendToAnotherPostRandom);
                        anotherPost.numberParcels.addAndGet(numberSendToAnotherPostRandom);
                        logger.info(namePost + " Gives to " + anotherPost.namePost + " " + numberSendToAnotherPostRandom);
                        logger.info(namePost + " Supper begin");
                        logger.info(namePost + " numberParcels: " + numberParcels);
                        Thread.sleep(delaySupperReceiver);
                        logger.info(namePost + " Supper end");
                        logger.info(namePost + " numberParcels: " + numberParcels);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
    //Если принимающий 1 почты видит, что у него уже больше 80 посылок и увольнение не за горами,
// то он увеличивает свой обед до 6с, чтобы меньше посылок принять от людей.
//Если же у него посылок менее 20, то он почти уверен в своей победе и уменьшает обед до 4с.
//Отправляющий 1 почты при количестве посылок больше 75 уменьшает свой обед до 5с, чтобы больше посылок отправить.
//А если посылок осталось менее 25, то увеличивает до 7с.
//
//Для 2 почты принимающий при более 79 посылок увеличивает обед до 6,5с, при менее 19 посылок уменьшает до 4с.
//Для 2 почты отправляющий при более 72 посылок уменьшает обед до 4,5с, при менее 23 посылок увеличивает до 6,5с.
    private Runnable runnableReceiverSupperChecker =
            () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (numberParcels.get() > numberIncreaseSupperReceiver) {
                            delaySupperReceiver = delayIncreaseSupperReceiver;
                            logger.info(namePost + " increase supper receiver to: " + delaySupperReceiver);
                        }
                        if (numberParcels.get() < numberDecreaseSupperReceiver) {
                            delaySupperReceiver = delayDecreaseSupperReceiver;
                            logger.info(namePost + " decrease supper receiver to: " + delaySupperReceiver);
                        }
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            };
    private Runnable runnableSenderSupperChecker =
            () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (numberParcels.get() > numberIncreaseSupperSender) {
                            delaySupperSender = delayIncreaseSupperSender;
                            logger.info(namePost + " increase supper sender to: " + delaySupperSender);
                        }
                        if (numberParcels.get() < numberDecreaseSupperSender) {
                            delaySupperSender = delayDecreaseSupperSender;
                            logger.info(namePost + " decrease supper sender to: " + delaySupperSender);
                        }
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            };
    private Runnable runnableSender =
            () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        for (int i = 0; i < countOfOperationBeforeSupperSender; i++) {
                            int numberParcelsSender = new Random().nextInt(numberMaxRandomParcelsSender) + 1;
                            numberParcels.addAndGet(-numberParcelsSender);
                            logger.info(namePost + " send: " + numberParcelsSender);
                            logger.info(namePost + " numberParcels: " + numberParcels);
                            Thread.sleep(delaySender);
                        }
                        int numberSendToAnotherPostRandom = new Random().nextInt(numberSendParcelsToAnotherPost);
                        numberParcels.addAndGet(-numberSendToAnotherPostRandom);
                        anotherPost.numberParcels.addAndGet(numberSendToAnotherPostRandom);
                        logger.info(namePost + " Gives to " + anotherPost.namePost + " " + numberSendToAnotherPostRandom);
                        logger.info(namePost + " Sender supper begin");
                        logger.info(namePost + " numberParcels: " + numberParcels);
                        Thread.sleep(delaySupperSender);
                        logger.info(namePost + " Sender supper end");
                        logger.info(namePost + " numberParcels: " + numberParcels);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
    //Тот, кто первым отправит все посылки со своего отделения, тот получает премию от почты России.
// Если же число посылок превысит 100, то операторов этой почты уволят за плохую медленную работу.
    private Runnable runnableCheckerLooseWin =
            () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (numberParcels.get() <= winNumberParcels) {
                            winLoose = true;
                            logger.info(namePost + " win less then " + winNumberParcels);
                            allStopPlusAnotherPost();
                            break;
                        }
                        if (numberParcels.get() >= looseNumberParcels) {
                            winLoose = false;
                            logger.info(namePost + " loose more then " + looseNumberParcels);
                            allStopPlusAnotherPost();
                            break;
                        }
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };

    private Thread receiver = new Thread(runnableReceiver);

    private Thread receiverChecker = new Thread(runnableReceiverSupperChecker);

    private Thread senderChecker = new Thread(runnableSenderSupperChecker);
    private Thread checkerLooseWin = new Thread(runnableCheckerLooseWin);
    private Thread sender = new Thread(runnableSender);
    //Если спустя 2 минуты состязание почтальонов не окончилось, то приз получает тот, кто отправил больше всего посылок.
    private Runnable runnableChecker =
            () -> {
                logger.info(namePost+ " competition counter run");
                try {
                    Thread.sleep(timeForFinish);
                } catch (InterruptedException e) {
                    return;
                }
                logger.info("Time is up");
                allStopPlusAnotherPost();
            };
    private Thread checker = new Thread(runnableChecker);

    private void allStop() {
        receiver.interrupt();
        receiverChecker.interrupt();
        sender.interrupt();
        senderChecker.interrupt();
        checker.interrupt();
        checkerLooseWin.interrupt();
    }

    private void allStopPlusAnotherPost() {
        anotherPost.allStop();
        allStop();
    }


    public void start() {
        receiver.start();
        sender.start();
        receiverChecker.start();
        senderChecker.start();
        checkerLooseWin.start();
        checker.start();
    }

    public void setAnotherPost(Post anotherPost) {
        this.anotherPost = anotherPost;
    }

    public Boolean getWinLoose() {
        return winLoose;
    }

    public AtomicInteger getNumberParcels() {
        return numberParcels;
    }

    public Thread getChecker() {
        return checker;
    }

//3
//Есть отделение почты номер 1. Оно принимает и отправляет посылки.
// На почте два оператора. Один принимает посылки - за раз он может принять от 1 до 3 посылок от одного человека.
// Второй отправляет посылки - за раз может отправить от 1 до 5 посылок.
// Время на операцию для принимающего - 500мс, для отправляющего - 600мс.
// После каждых 20 операций принимающий уходит на обед на 5с.
// Отправляющий после 25 операций уходит на обед на 6с. Потом все повторяется.
// Изначально на почте 50 посылок.
//
// Есть отделение почты номер 2. Здесь все аналогично, только цифры другие.
// Принять за раз может от 1 до 2 посылок от одного человека. Отправить за раз может от 1 до 4 посылок.
// Время на операцию отправки - 450мс, приема - 550мс. После каждых 23 операций приема оператор уходит на обед на 6с.
// После каждых 27 операций отправки оператор уходит на обед на 5с.
// Изначально на почте 55 посылок.
//
//Тот, кто первым отправит все посылки со своего отделения, тот получает премию от почты России.
// Если же число посылок превысит 100, то операторов этой почты уволят за плохую медленную работу.
//
//Поэтому операторы из этих двух отделений почты конкуренты друг другу. И ради заветного приза готовы на разные гадости.
//А именно - во время обеда операторы 1 почты могут взять от 0 до 5 посылок со своей почты и подбросить конкуренту. Для 2 почты - от 0 до 7 посылок.


//Если принимающий 1 почты видит, что у него уже больше 80 посылок и увольнение не за горами,
// то он увеличивает свой обед до 6с, чтобы меньше посылок принять от людей.
//Если же у него посылок менее 20, то он почти уверен в своей победе и уменьшает обед до 4с.
//Отправляющий 1 почты при количестве посылок больше 75 уменьшает свой обед до 5с, чтобы больше посылок отправить.
//А если посылок осталось менее 25, то увеличивает до 7с.
//
//Для 2 почты принимающий при более 79 посылок увеличивает обед до 6,5с, при менее 19 посылок уменьшает до 4с.
//Для 2 почты отправляющий при более 72 посылок уменьшает обед до 4,5с, при менее 23 посылок увеличивает до 6,5с.
//Если спустя 2 минуты состязание почтальонов не окончилось, то приз получает тот, кто отправил больше всего посылок.

    public static void main(String[] args) throws InterruptedException {
        Post post = new Post("Post №1", new AtomicInteger(50), 20, 25,
                3, 5, 500, 600, 5000,
                6000, 5, 80,
                75, 6000, 4000, 20,
                25, 5000, 7000);
        Post anotherPost = new Post("Post №2", new AtomicInteger(55), 23, 27,
                2, 4, 450, 550, 6000,
                5000, 7, 79, 72,
                6500, 4500, 19, 23,
                4000, 6500);
        post.setAnotherPost(anotherPost);
        anotherPost.setAnotherPost(post);
        post.start();
        anotherPost.start();
        post.getChecker().join();
        Boolean winLoose = post.getWinLoose();
        if (winLoose == null) {
            if (post.getNumberParcels().get() > anotherPost.getNumberParcels().get()) {
                logger.info("winFirst more number parcels");
            } else if (post.getNumberParcels().get() < anotherPost.getNumberParcels().get()) {
                logger.info("firstLoose less number parcels");
            } else {
                logger.info("Both loose and win");
            }
        } else {
            if (winLoose) {
                logger.info("winFirst");
            } else {
                logger.info("firstLoose");
            }
        }
    }

}