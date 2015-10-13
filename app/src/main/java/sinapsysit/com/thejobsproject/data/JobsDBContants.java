package sinapsysit.com.thejobsproject.data;

import android.provider.BaseColumns;

/**
 * Created by jujomago on 12/10/2015.
 */
public class JobsDBContants {
    public static class JobDbData implements BaseColumns {
        public static final String TABLE_NAME = "jobs";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_POSTED_DATE = "posted_date";
    }

    public static class ContactDbData implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_JOB_ID = "job_id";
        public static final String COLUMN_NUMBER = "number";
    }
}
