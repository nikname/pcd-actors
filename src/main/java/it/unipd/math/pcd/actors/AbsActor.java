/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    // MailBox methods are not synchronized. Access must be synchronized
    private final MailBox<T> mailBox = new MailBox<T>();

    private volatile boolean interrupted = false;

    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    /**
     * Stores a new message into the mailbox.
     *
     * @param message Message received
     * @param sender Sender of the message
     * @return boolean true if the message was stored; false otherwise
     */
    public final boolean storeMessage(T message, ActorRef<T> sender) {
        boolean success = false;

        if(!interrupted) {
            synchronized (mailBox) {
                success = mailBox.add(message, sender);
                mailBox.notifyAll();
            }
        }

        return success;
    }

    /**
     * Set to true the interrupted status flag of the actor.
     */
    public void interrupt() {
        interrupted = true;
        synchronized (mailBox) {
            mailBox.notifyAll();
        }
    }

    /**
     * Redefinition of the default constructor.
     */
    public AbsActor() {
        new Thread(new MessagesManager()).start();
    }

    /**
     * Manages messages received by an actor.
     *
     * @author Nicola Dalla Costa
     * @version 1.0
     * @since 1.0
     */
    class MessagesManager implements Runnable {

        @Override
        public void run() {
            while(!interrupted) {
                executeMessage();
            }

            flushInbox();
        }

        private void executeMessage() {
            MailBox<T>.MailBoxItem item;

            synchronized (mailBox) {
                while(mailBox.isEmpty()) {
                    try {
                        mailBox.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                item = mailBox.remove();
            }

            sender = item.getSender();
            receive(item.getMessage());
        }

        private void flushInbox() {
            synchronized (mailBox) {
                while(!mailBox.isEmpty()) {
                    executeMessage();
                }
            }
        }

    }

}
