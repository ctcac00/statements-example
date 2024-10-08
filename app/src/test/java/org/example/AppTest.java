/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import static com.mongodb.client.model.Filters.eq;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

class AppTest {
  @Test
  void GridFSTest() throws FileNotFoundException, IOException {

    MongoClient mongoClient;

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb");
    database.drop();

    GridFSBucket gridFSBucket = GridFSBuckets.create(database);

    /*
     * UploadFromStream Example
     */
    // Get the input stream
    InputStream streamToUploadFrom = new ByteArrayInputStream(
        "Hello World".getBytes(StandardCharsets.UTF_8));

    // Create some custom options
    GridFSUploadOptions options = new GridFSUploadOptions()
        .chunkSizeBytes(1024)
        .metadata(new Document("type", "presentation"));

    ObjectId fileId = gridFSBucket.uploadFromStream("mongodb-tutorial", streamToUploadFrom, options);
    streamToUploadFrom.close();
    System.out.println("The fileId of the uploaded file is: " + fileId.toHexString());

    /*
     * OpenUploadStream Example
     */

    // Get some data to write
    byte[] data = "Data to upload into GridFS".getBytes(StandardCharsets.UTF_8);

    GridFSUploadStream uploadStream = gridFSBucket.openUploadStream("sampleData");
    uploadStream.write(data);
    uploadStream.close();
    System.out.println("The fileId of the uploaded file is: " + uploadStream.getObjectId().toHexString());

    /*
     * Find documents
     */
    gridFSBucket.find().forEach(gridFSFile -> System.out.println(gridFSFile.getFilename()));

    /*
     * Find documents with a filter
     */
    gridFSBucket.find(eq("metadata.contentType", "image/png")).forEach(
        gridFSFile -> System.out.println(gridFSFile.getFilename()));

    /*
     * DownloadToStream
     */
    FileOutputStream streamToDownloadTo = new FileOutputStream("/tmp/mongodb-tutorial.txt");
    gridFSBucket.downloadToStream(fileId, streamToDownloadTo);
    streamToDownloadTo.close();

    /*
     * DownloadToStreamByName
     */
    streamToDownloadTo = new FileOutputStream("/tmp/mongodb-tutorial.txt");
    GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
    gridFSBucket.downloadToStream("mongodb-tutorial", streamToDownloadTo, downloadOptions);
    streamToDownloadTo.close();

    /*
     * OpenDownloadStream
     */
    GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId);
    int fileLength = (int) downloadStream.getGridFSFile().getLength();
    byte[] bytesToWriteTo = new byte[fileLength];
    downloadStream.read(bytesToWriteTo);
    downloadStream.close();

    System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));

    /*
     * OpenDownloadStreamByName
     */

    downloadStream = gridFSBucket.openDownloadStream("sampleData");
    fileLength = (int) downloadStream.getGridFSFile().getLength();
    bytesToWriteTo = new byte[fileLength];
    downloadStream.read(bytesToWriteTo);
    downloadStream.close();

    System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));

    /*
     * Rename
     */
    gridFSBucket.rename(fileId, "mongodbTutorial");

    /*
     * Delete
     */
    // gridFSBucket.delete(fileId);

    // database.drop();

  }
}
