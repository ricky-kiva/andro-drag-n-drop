package com.rickyslash.dragdrop

import android.content.ClipData
import android.content.ClipDescription
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.rickyslash.dragdrop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.llTop.setOnDragListener(dragListener)
        binding.llBottom.setOnDragListener(dragListener)

        binding.viewBall.setOnLongClickListener {
            val clipText = "Goal! Good job Messi!" // content for the data
            val item = ClipData.Item(clipText) // contains the data that will be drag & dropped (placed on clipboard)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)  // type of data that is being put to the clipboard
            val data = ClipData(clipText, mimeTypes, item) // packages of the data to copy

            val dragShadow = View.DragShadowBuilder(it) // create shadow during drag & drop
            it.startDragAndDrop(data, dragShadow, it, 0) // initiate the drag & drop operation

            it.visibility = View.INVISIBLE // set this object to be invisible while drag & dropped
            true
        }

        binding.viewBallTwo.setOnLongClickListener {
            val clipText = "Goal! Good job Ronaldo!"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadow = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadow, it, 0)

            it.visibility = View.INVISIBLE
            true
        }

        binding.viewBallThree.setOnLongClickListener {
            val clipText = "Goal! Good job Ronaldo!"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadow = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadow, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
    }

    // `v` is the destination view
    // `event` encapsulates information about the drag-n-drop operation
    private val dragListener = View.OnDragListener { v, event ->
        when(event.action) {

            // when drag operation starts, this triggered
            DragEvent.ACTION_DRAG_STARTED -> {
                // check if the dragged data has a MIME type of plain text, not continuing the process if false
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }

            // when dragged item enters the bounds of the view, this triggered
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.invalidate() // redraw the view
                true
            }

            // when drag location changes but no other specific action is needed, this trigggered
            DragEvent.ACTION_DRAG_LOCATION -> true

            // when drag leaves the bounds of the view, this triggered
            DragEvent.ACTION_DRAG_EXITED -> {
                v.invalidate() // redraw the view
                true
            }

            // when user release the dragged item, this triggered
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0) // retrieve the first item from ClipData
                val dragData = item.text // get first item content
                Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

                v.invalidate() // redraw the view (call this before making changes in view's layout)

                val dragObject = event.localState as View // get the dragged item view
                val dragParent = dragObject.parent as ViewGroup // get the dragged item parent
                dragParent.removeView(dragObject) // remove the dragged item from its parent

                val destination = v as LinearLayout // set destination view as linear layout

                val x = event.x
                val y = event.y

                val childCount = destination.childCount
                var index = -1

                if (childCount > 0) {
                    val firstChild = destination.getChildAt(0)
                    val lastChild = destination.getChildAt(childCount - 1)
                    val childHalfHeight = firstChild.height / 2
                    val childHalfWidth = firstChild.width / 2

                    if (y < firstChild.y + childHalfHeight && x < firstChild.x + childHalfWidth) {
                        index = 0 // Drop position is to the left of the first child
                    } else if (y > lastChild.y + childHalfHeight && x > lastChild.x + childHalfWidth) {
                        index = childCount // Drop position is to the right of the last child
                    } else {
                        for (i in childCount - 1 downTo 0) {
                            val child = destination.getChildAt(i)
                            val childHalfHeight = child.height / 2
                            val childHalfWidth = child.width / 2

                            if (y < child.y + childHalfHeight && x < child.x + childHalfWidth) {
                                index = i
                                break
                            }
                        }
                    }
                }

                if (index != -1) {
                    destination.addView(dragObject, index)
                } else {
                    destination.addView(dragObject)
                }

                // destination.addView(dragObject) // add dragged item to the destination

                dragObject.visibility = View.VISIBLE // change visibility of dragged object
                true
            }

            // when drag operation ends
            DragEvent.ACTION_DRAG_ENDED -> {
                v.invalidate() // redraw the view
                true
            }

            else -> false
        }
    }
}

/* Note: Adding multiple items

val clipText1 = "Item 1"
val clipText2 = "Item 2"

val item1 = ClipData.Item(clipText1)
val item2 = ClipData.Item(clipText2)

val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

val data = ClipData("Multiple Items", mimeTypes, item1)
data.addItem(item2)
*/