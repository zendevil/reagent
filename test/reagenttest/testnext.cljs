(ns reagenttest.testnext
  (:require [clojure.test :as t :refer-macros [is deftest testing]]
            [reagent.dom.server :as server]
            [reagent.next :as r]))

(defn rstr [react-elem]
  (server/render-to-static-markup react-elem))

(deftest really-simple-test
  (testing "simple element"
    (is (= "<p class=\"bar\">foo</p>"
         (rstr (r/as-element nil [:p {:className "bar"} "foo"])))))

  (testing "simple component"
    (let [comp (fn [props]
                 [:div nil (aget (.-children props) 0)])]
      (is (= "<div>Hello</div>"
             (rstr (r/as-element nil [comp nil "Hello" "World"])))))))
