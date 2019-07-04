package com.jelly.thor.rxhttp.builder;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.UploadFileRequest;

import java.io.File;
import java.util.List;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/6 16:54 <br/>
 */
public class UploadFileBuilder extends BaseBuilder<UploadFileRequest>{
    private List<FileInput> file;

    public List<FileInput> getFile() {
        return file;
    }

    public UploadFileBuilder files(@NonNull List<FileInput> fileInputList) {
        this.file = fileInputList;
        return this;
    }

    @Override
    public UploadFileRequest build(@NonNull BaseCallback callback) {
        if (file == null){
            throw new IllegalArgumentException("请先调用files方法");
        }
        return new UploadFileRequest(this, callback);
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
