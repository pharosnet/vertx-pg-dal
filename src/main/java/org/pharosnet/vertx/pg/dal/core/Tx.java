package org.pharosnet.vertx.pg.dal.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.sqlclient.Transaction;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class Tx {

    private static final Logger log = LoggerFactory.getLogger(Tx.class);

    public Tx(Transaction tx) {
        this.tx = tx;
    }

    private Transaction tx;

    private void rollback(Throwable cause, Handler<AsyncResult<Void>> handler) {
        this.tx.rollback(r -> {
            if (r.failed()) {
                log.error("tx rollback failed", r.cause());
            }
            this.tx.close();
            handler.handle(Future.failedFuture(cause));
        });
    }

    public void rollback(Handler<AsyncResult<Void>> handler) {
        this.tx.rollback(r -> {
            if (r.failed()) {
                log.error("tx rollback failed", r.cause());
                handler.handle(Future.failedFuture(r.cause()));
            }
            handler.handle(Future.succeededFuture());
        });
    }

    public void close() {
        this.tx.close();
    }


    public void commit(Handler<AsyncResult<Void>> handler) {
        this.tx.commit(r -> {
            if (r.failed()) {
                log.error("tx commit failed", r.cause());
                this.rollback(r.cause(), handler);
                return;
            }
            this.close();
            handler.handle(Future.succeededFuture());
        });
    }

    public void update(String ql, Handler<AsyncResult<Integer>> handler) {
        this.tx.query(ql, r -> {
            if (r.failed()) {
                log.error("tx update failed", r.cause());
                if (log.isDebugEnabled()) {
                    log.debug("tx update:\n\tsql:{}", ql);
                }
                this.rollback(r.cause(), rb -> {
                    handler.handle(Future.failedFuture(rb.cause()));
                });
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("tx update:\n\tsql:{}\n\tupdated:{}", ql, r.result().rowCount());
            }
            handler.handle(Future.succeededFuture(r.result().rowCount()));
        });
    }

    public void update(String ql, Tuple args, Handler<AsyncResult<Integer>> handler) {
        this.tx.preparedQuery(ql, args, r -> {

            if (r.failed()) {
                log.error("tx update failed", r.cause());
                if (log.isDebugEnabled()) {
                    log.debug("tx update:\n\tsql:{}\n\targs:{}", ql, args.toString());
                }
                this.rollback(r.cause(), rb -> {
                    handler.handle(Future.failedFuture(rb.cause()));
                });
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("tx update:\n\tsql:{}\n\targs:{}\n\tupdated:{}", ql, args.toString(), r.result().rowCount());
            }
            handler.handle(Future.succeededFuture(r.result().rowCount()));
        });
    }

    public void updateBatch(String ql, List<Tuple> args, Handler<AsyncResult<Integer>> handler) {
        this.tx.preparedBatch(ql, args, r -> {
            if (r.failed()) {
                log.error("tx update batch failed", r.cause());
                if (log.isDebugEnabled()) {
                    log.debug("tx update batch:\n\tsql:{}\n\targs:{}", ql, args.toString());
                }
                this.rollback(r.cause(), rb -> {
                    handler.handle(Future.failedFuture(rb.cause()));
                });
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("tx update batch:\n\tsql:{}\n\targs:{}\n\tupdated:{}", ql, args.toString(), r.result().rowCount());
            }
            handler.handle(Future.succeededFuture(r.result().rowCount()));
        });
    }

}
