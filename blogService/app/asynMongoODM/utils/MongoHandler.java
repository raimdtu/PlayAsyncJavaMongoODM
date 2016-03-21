package asynMongoODM.utils;

import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by roshan on 19/03/16.
 */
public class MongoHandler<T> {

    public CompletionStage<?> readOneDocument(MongoCollection<T> collection,Bson filters){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        if(filters==null)
            collection.find().first(getSingleFindResultCallBack(completionStage));
        else
            collection.find(filters).first(getSingleFindResultCallBack(completionStage));
        return completionStage;
    }


    public SingleResultCallback<T> getSingleFindResultCallBack(CompletionStage<T> completionStage){
        SingleResultCallback<T> singleResultCallback=new SingleResultCallback<T>() {
            @Override
            public void onResult(T o, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Object not fetched due to error " +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else {
                    Logger.info("Object fetched is " + o);
                    ((CompletableFuture) completionStage).complete(o);
                }

            }
        };
                return singleResultCallback;
    }

    public CompletionStage<?> readDocuments(MongoCollection<T> collection,Bson filters){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        List<T> list=new ArrayList<>();
        if(filters==null)
            collection.find().forEach(processEachObject(list),getCallbackWhenFindManyFinished(completionStage,list));
        else
            collection.find(filters).forEach(processEachObject(list),getCallbackWhenFindManyFinished(completionStage,list));
        return completionStage;
    }

    public Block<T> processEachObject(List<T> list) {
        Block<T>  printDocumentBlock = new Block<T>() {
            @Override
            public void apply(final T object) {
                Logger.info("Object fetched is " + object);
                list.add(object);
            }
        };
        return printDocumentBlock;
    }

    public SingleResultCallback<Void> getCallbackWhenFindManyFinished(CompletionStage<T> completionStage,List<T> list){
        SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Object not inserted due to error " +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("List of Object is sucessfully fetched");
                    ((CompletableFuture)completionStage).complete(list);
                }
            }
        };
        return callbackWhenFinished;
    }


    public CompletionStage<?> insertOneDocuments(MongoCollection<T> collection,T object){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.insertOne(object, getSingleInsertResultCallBack(completionStage, object));
        return completionStage;
    }

    public SingleResultCallback<Void> getSingleInsertResultCallBack(CompletionStage<T> completionStage,T object){
        SingleResultCallback<Void> singleResultCallback=new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void result, Throwable throwable) {
                   if(throwable!=null){
                    Logger.error("Object not inserted due to error " +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                   }else{
                       Logger.info("Object inserted is "+object);
                       ((CompletableFuture)completionStage).complete(object);
                   }
            }
        };
        return singleResultCallback;
    }

    public CompletionStage<?> insertManyDocuments(MongoCollection<T> collection,List<T> objects){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.insertMany(objects, getMultipleInsertResultCallBack(completionStage, objects));
        return completionStage;
    }

    public SingleResultCallback<Void> getMultipleInsertResultCallBack(CompletionStage<T> completionStage,List<T> objects){
        SingleResultCallback<Void> singleResultCallback=new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void result, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Objects not inserted due to error " +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Objects inserted is "+objects);
                    ((CompletableFuture)completionStage).complete(objects);
                }
            }
        };
        return singleResultCallback;
    }

    public CompletionStage<?> countDocuments(MongoCollection<T> collection,Bson filters){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.count(filters, getCountResultCallBack(completionStage, filters));
        return completionStage;
    }


    public SingleResultCallback<Long> getCountResultCallBack(CompletionStage<T> completionStage,Bson filters){
        SingleResultCallback<Long> singleResultCallback=new SingleResultCallback<Long>() {
            @Override
            public void onResult(Long count, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Error during count for "+filters +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Object count for "+filters +" is "+count);
                    ((CompletableFuture)completionStage).complete(count);
                }
            }
        };
        return singleResultCallback;
    }

    public CompletionStage<?> updateOneDocuments(MongoCollection<T> collection,Bson filters,Bson update){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.updateOne(filters,update, getUpdateOneResultCallBack(completionStage, filters, update));
        return completionStage;
    }

    public SingleResultCallback<UpdateResult> getUpdateOneResultCallBack(CompletionStage<T> completionStage,Bson filters,Bson update){
        SingleResultCallback<UpdateResult> singleResultCallback=new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(UpdateResult result, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Error during "+"to update "+update+" for "+filters +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Sucessfully updated "+update+" for "+filters);
                    ((CompletableFuture)completionStage).complete(result);
                }
            }
        };
        return singleResultCallback;
    }


    public CompletionStage<?> updateManyDocuments(MongoCollection<T> collection,Bson filters,Bson update){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.updateMany(filters, update, getUpdateManyResultCallBack(completionStage, filters, update));
        return completionStage;
    }

    public SingleResultCallback<UpdateResult> getUpdateManyResultCallBack(CompletionStage<T> completionStage,Bson filters,Bson update){
        SingleResultCallback<UpdateResult> singleResultCallback=new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(UpdateResult result, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Error during "+"to update "+update+" for "+filters +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Sucessfully updated "+update+" for "+filters);
                    ((CompletableFuture)completionStage).complete(result);
                }
            }
        };
        return singleResultCallback;
    }

    public CompletionStage<?> deleteOneDocuments(MongoCollection<T> collection,Bson filters){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.deleteOne(filters, getDeleteOneResultCallBack(completionStage, filters));
        return completionStage;
    }

    public SingleResultCallback<DeleteResult> getDeleteOneResultCallBack(CompletionStage<T> completionStage,Bson filters){
        SingleResultCallback<DeleteResult> singleResultCallback=new SingleResultCallback<DeleteResult>() {
            @Override
            public void onResult(DeleteResult result, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Error during "+"delete for "+filters +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Sucessfully Deleted for "+filters);
                    ((CompletableFuture)completionStage).complete(result);
                }
            }
        };
        return singleResultCallback;
    }

    public CompletionStage<?> deleteManyDocuments(MongoCollection<T> collection,Bson filters){
        CompletionStage<T> completionStage=new CompletableFuture<>();
        collection.deleteMany(filters, getDeleteManyResultCallBack(completionStage, filters));
        return completionStage;
    }

    public SingleResultCallback<DeleteResult> getDeleteManyResultCallBack(CompletionStage<T> completionStage,Bson filters){
        SingleResultCallback<DeleteResult> singleResultCallback=new SingleResultCallback<DeleteResult>() {
            @Override
            public void onResult(DeleteResult result, Throwable throwable) {
                if(throwable!=null){
                    Logger.error("Error during "+"delete for "+filters +throwable.getMessage());
                    ((CompletableFuture)completionStage).completeExceptionally(throwable);
                }else{
                    Logger.info("Sucessfully Deleted for "+filters);
                    ((CompletableFuture)completionStage).complete(result);
                }
            }
        };
        return singleResultCallback;
    }



}
