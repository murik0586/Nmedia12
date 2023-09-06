//package ru.netology.nmedia.dao
//
//import android.content.ContentValues
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import ru.netology.nmedia.dto.Post
//
//class PostDaoImpl(private val db: SQLiteDatabase)  {
//
//    companion object {
//        val DDL = """
//            CREATE TABLE ${PostColumns.TABLE} (
//            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
//            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
//            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
//            ${PostColumns.COLUMN_LIKES} INTEGER DEFAULT 0,
//            ${PostColumns.COLUMN_SHARES} INTEGER DEFAULT 0,
//            ${PostColumns.COLUMN_WATCHES} INTEGER DEFAULT 0,
//            ${PostColumns.COLUMN_LIKEDBYME} BOOLEAN NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_SHAREDBYME} BOOLEAN NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_VIDEOURL} TEXT DEFAULT ""
//            );
//            """.trimIndent()
//    }
//
//    object PostColumns {
//        const val TABLE = "posts"
//        const val COLUMN_ID = "id"
//        const val COLUMN_AUTHOR = "author"
//        const val COLUMN_CONTENT = "content"
//        const val COLUMN_PUBLISHED = "published"
//        const val COLUMN_LIKES = "likes"
//        const val COLUMN_SHARES = "shares"
//        const val COLUMN_WATCHES = "watches"
//        const val COLUMN_LIKEDBYME = "likedByMe"
//        const val COLUMN_SHAREDBYME = "sharedByMe"
//        const val COLUMN_VIDEOURL = "videoUrl"
//        val ALL_COLUMNS = arrayOf(
//            COLUMN_ID,
//            COLUMN_AUTHOR,
//            COLUMN_CONTENT,
//            COLUMN_PUBLISHED,
//            COLUMN_LIKES,
//            COLUMN_SHARES,
//            COLUMN_WATCHES,
//            COLUMN_LIKEDBYME,
//            COLUMN_SHAREDBYME,
//            COLUMN_VIDEOURL
//        )
//    }
//
//    override fun getAll(): List<Post> {
//        val posts = mutableListOf<Post>()
//        db.query(
//            PostColumns.TABLE,
//            PostColumns.ALL_COLUMNS,
//            null,
//            null,
//            null,
//            null,
//            "${PostColumns.COLUMN_ID} DESC"
//        ).use {
//            while (it.moveToNext()) {
//                posts.add(map(it))
//            }
//        }
//        return posts
//    }
//
//    override fun likeById(id: Long) {
//        db.execSQL(
//            """
//                UPDATE posts SET
//                    likes = likes + CASE WHEN likedByMe THEN -1 ELSE + 1 END,
//                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
//                WHERE id = ?
//            """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun shareById(id: Long) {
//        db.execSQL(
//            """
//                UPDATE posts SET
//                    shares = shares + CASE WHEN sharedByMe THEN -1 ELSE + 1 END,
//                    sharedByMe = CASE WHEN sharedByMe THEN 0 ELSE 1 END
//                WHERE id = ?
//            """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun watchById(id: Long) {
//        db.execSQL(
//            """
//                UPDATE posts SET
//                    watches = watches + 1 END
//                WHERE id = ?
//            """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun save(post: Post) : Post {
//        val values = ContentValues().apply {
//            if (post.id != 0L) {
//                put(PostColumns.COLUMN_ID, post.id)
//            }
//
//            put(PostColumns.COLUMN_AUTHOR, "Me")
//            put(PostColumns.COLUMN_CONTENT, post.content)
//            put(PostColumns.COLUMN_PUBLISHED, "now")
//        }
//
//        val id = db.replace(PostColumns.TABLE, null, values)
//        db.query(
//            PostColumns.TABLE,
//            PostColumns.ALL_COLUMNS,
//            "${PostColumns.COLUMN_ID} = ?",
//            arrayOf(id.toString()),
//            null,
//            null,
//            null
//        ).use {
//            it.moveToNext()
//            return map(it)
//        }
//    }
//
//    override fun removeById(id: Long) {
//        db.delete(
//            PostColumns.TABLE,
//            "${PostColumns.COLUMN_ID} = ?",
//            arrayOf(id.toString())
//        )
//    }
//
//    private fun map(cursor : Cursor) : Post {
//        with(cursor) {
//            return Post(
//                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
//                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
//                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
//                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
//                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
//                shares = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
//                watches = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_WATCHES)),
//                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKEDBYME)) != 0,
//                sharedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)) != 0,
//                videoUrl = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEOURL))
//            )
//        }
//    }
//}