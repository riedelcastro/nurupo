package org.riedelcastro.nurupo

import collection.mutable.HashMap
import collection.MapProxy

/**
 * Simple bidirectional map.
 * @author sriedel
 */
class BiDiMap[K, V] extends collection.mutable.Map[K, V] {
  private val _forward = new HashMap[K, V]
  private val _backward = new HashMap[V, K]

  def forward:collection.Map[K,V] = _forward
  def backward:collection.Map[V,K] = _backward


  def get(key: K) = _forward.get(key)
  def iterator = _forward.iterator

  def +=(kv: (K, V)) = {
    _forward += kv
    _backward += kv.swap
    this
  }

  def -=(key: K) = {
    for (v <- _forward.get(key)) {
      _forward -= key
      _backward -= v
    }
    this
  }
}
