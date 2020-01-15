(ns reagent.next
  (:require ["react" :as react]
            [goog.object :as gobj]))

(defprotocol Compiler
  (create-element [this form]))

(defprotocol IElement
  (-as-element [this]))

(defprotocol IComponent
  (-as-component [x]))

(defprotocol IProps
  (-as-props [this]))

(defn map-to-js-obj [m]
  (reduce-kv (fn [obj k v]
               (gobj/set obj (name k) v)
               obj)
             #js {}
             m))

(defn wrap-fn [c]
  (fn [props]
    (-as-element (c props))))

(defn vec-to-elem [v]
  (let [c (get v 0)
        ;; TODO: Optional props.
        props (get v 1)
        children (subvec v 2)]
    (apply react/createElement (-as-component c) (-as-props props) children)))

(extend-protocol IProps
  PersistentArrayMap
  (-as-props [this] (map-to-js-obj this))
  PersistentHashMap
  (-as-props [this] (map-to-js-obj this))
  default
  (-as-props [this] this))

(extend-protocol IComponent
  Keyword
  (-as-component [tag] (name tag))
  string
  (-as-component [tag] tag)
  function
  (-as-component [c] (wrap-fn c)))

(extend-protocol IElement
  ISeq
  (-as-element [x]
    (into-array (map -as-element x)))
  PersistentVector
  (-as-element [v] (vec-to-elem v))
  default
  (-as-element [x] x))

(defn as-element [compiler hiccup]
  (-as-element hiccup))

(defn create-compiler [options]
  #_
  (reify Compiler
    ))
