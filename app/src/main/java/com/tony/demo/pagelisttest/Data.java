package com.tony.demo.pagelisttest;

import android.support.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author tony
 */
@Entity
public class Data {

    @Id
    public long id;

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj instanceof Data) {
            Data data = (Data) obj;
            if (this.id == data.id && this.content.equals(data.content)) {
                return true;
            }
        }
        return false;
    }
}
