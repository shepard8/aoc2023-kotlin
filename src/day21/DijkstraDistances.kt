package day21

import java.util.PriorityQueue
import kotlin.math.max

class DijkstraDistances(private val grid: Grid, initialPosition: Cell): HashMap<Cell, Int>() {

    private var maxDistance = 0

    private data class Pair(val position: Cell, val distance: Int)

    init {
        val pq = PriorityQueue<Pair> { x, y -> x.distance - y.distance }
        this[initialPosition] = 0
        pq.add(Pair(initialPosition, 0))
        compute(pq)
    }

    private fun compute(pq: PriorityQueue<Pair>) {
        while (pq.isNotEmpty()) {
            val (position, distance) = pq.remove()
            maxDistance = max(maxDistance, distance)
            for (link in position.neighbours().filter { !this.containsKey(it) }) {
                this[link] = distance + 1
                pq.add(Pair(link, distance + 1))
            }
        }
    }

    fun maxDistance() = maxDistance

}