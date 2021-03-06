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
 * @author Nicola Dalla Costa
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Defines the structure of the mailbox.
 *
 * @author Nicola Dalla Costa
 * @version 1.0
 * @since 1.0
 */
public class MailBox<T extends Message> {

    // BlockingQueue implementation is not synchronized. Access must be synchronized
    private final BlockingQueue<MailBoxItem> queue = new LinkedBlockingQueue<>();

    /**
     * Adds a new MailBoxItem (message and sender) to the end of the queue.
     *
     * @param message Message received
     * @param sender Sender of the message
     */
    public void add(T message, ActorRef<T> sender) {
        synchronized (queue) {
            try {
                queue.put(new MailBoxItem(message, sender));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes and retrieves the oldest MailBoxItem (message and sender) form the queue.
     *
     * @return MailBoxItem (message and sender)
     */
    public MailBoxItem remove() {
        synchronized (queue) {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Checks if the mailbox is empty.
     *
     * @return true if the mailbox is empty; false otherwise
     */
    public boolean isEmpty() {
        synchronized (queue) {
            return queue.isEmpty();
        }
    }

    /**
     * Defines mailbox item structure.
     *
     * @author Nicola Dalla Costa
     * @version 1.0
     * @since 1.0
     */
    class MailBoxItem {

        private final T message;
        private final ActorRef<T> sender;

        /**
         * Two-arguments constructor.
         *
         * @param message Message received
         * @param sender Sender of the message
         */
        public MailBoxItem(T message, ActorRef<T> sender) {
            super();
            this.message = message;
            this.sender = sender;
        }

        /**
         * Retrieves the message associated to the MailBoxItem.
         *
         * @return Message received
         */
        public T getMessage() {
            return message;
        }

        /**
         * Retrieves the sender associated to the MailBoxItem.
         *
         * @return Sender of the message
         */
        public ActorRef<T> getSender() {
            return sender;
        }
    }
}
