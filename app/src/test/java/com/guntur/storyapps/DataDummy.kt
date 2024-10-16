package com.guntur.storyapps

import com.guntur.storyapps.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "create $i",
                "name $i",
                "description $i",
                i.toDouble(),
                "id $i",
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}