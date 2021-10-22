import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.SerializedSubscriber;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RxJavaTest {


    @Test
    public void window() throws InterruptedException {
        AtomicInteger cou = new AtomicInteger();
        Observable.interval(1, TimeUnit.SECONDS )
                .take(10)
                .window(5,5, TimeUnit.SECONDS)
                .flatMap(i->{
                    System.out.println("=== "+ i);
                    return i.toList().toObservable();
                })
                /*.reduce(new BiFunction<Long, Long, Long>() {
                    @NonNull
                    @Override
                    public Long apply(@NonNull Long aLong, @NonNull Long aLong2) throws Exception {
                        System.out.println();
                        return aLong+aLong2;
                    }
                })*/
                .subscribe(i->{
                    System.out.println("---------"+cou.getAndIncrement());
                    System.out.println(i);
                })
        ;

        synchronized (RxJavaTest.class){
            RxJavaTest.class.wait();
        }
    }

    @Test
    public void window2() throws InterruptedException {
        Consumer push = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                ((Observer)o).onNext(o);
            }
        };
        Thread t = new Thread(()->{
            try {
                push.accept(122);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Observable<String> o = Observable.create(emitter -> new SendData<String>(emitter).exec());

        /*new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                System.out.println("call");
                return new ObservableSource() {
                    @Override
                    public void subscribe(@NonNull Observer observer) {
                        System.out.println("subcribe 123");
//                        observer.onNext(123);
//                        observer.onNext(232);
                        observer.onNext(1);

//                        new SendData(observer).exec();

                    }
                };
            }
        });*/

      Observable win = o.window(5,1, TimeUnit.SECONDS)
                .flatMap(i->{
                   // System.out.println("=== "+ i);
                    return i.toList().toObservable();
                });

        win.subscribe(i-> {
            System.out.println("accept = " + i);
        });

       /* new Thread(()->{
            while (true){
                try {
                    Thread.sleep(1000*10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("------ count " + o.count().blockingGet());
            }
        }).start();*/

        //o.subscribe(integer -> System.out.println("accept2 = " + integer));

        synchronized (RxJavaTest.class){
            RxJavaTest.class.wait();
        }
    }

    public static class SendData<T>{
        Thread t;
        ObservableEmitter<T> observer;
        public SendData(ObservableEmitter<T> observer){
            this.observer = observer;
            t = new Thread(this::doExec);
        }

        public void exec(){
            t.start();
        }

        AtomicInteger atomicInteger = new AtomicInteger();
        private void doExec(){
            while (true){
                long sleep = (long) new Random().nextInt(1000);
                sleep=200;
                //System.out.println("thread "+ Thread.currentThread().getName()+" ,  sleep " + sleep);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                observer.onNext((T) (atomicInteger.getAndIncrement() + " = " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))));
            }

        }
    }
}

