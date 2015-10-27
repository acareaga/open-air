(ns open-air.core
  (:require [cheshire.core :as json]
             [clj-http.client :as http]))

(def base-url "http://playlist.cprnetwork.org/api/playlistCO?n=")

(defn current-tracks-url []
  (str base-url (System/currentTimeMillis)))

(defn current-tracks []
  (json/parse-string
   (:body
    (http/get (current-tracks-url)))))

(defn format-track [t]
  (select-keys t
               ["album" "artist" "title" "start_time" "label" "label_num"]
               ))

(defn format-tracks [tracks]
  (map format-track tracks))

(def rdio-base-url "https://services.rdio.com/api/1/")

# dev app key
(defn consumer-key "p23lzdbzrngozekh4fzs5nvzku")
(defn consumer-secret "VvoFmmUmA4AUBcknbzuBJA")
(defn token "Bearer") # need to add oauth code

(defn rdio-query [params]
  (http/post
    ( str rdio-base-url "search"
    { :query-params { "query" query
                      "method" "search"
                      "types" "track" }
      :throw-exceptions false
      :headers {"Authorization" token }}))
