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

(def consumer-key "z47detwiazasnfqqkvr2lxjvce")
(def consumer-secret "BDFsBoy1BII0FdqRJR7E3Q")
(def token "Bearer AAAAAWEAAAAAAk1MdwAAAABWOLoNVjlizQAAABp1YXhucHl0N3FiYnI3bnQ0bHllZTN0czZ4dRHPJ275of0XM3fbqq6WyqAIOS5rrz7e5h-zqsxpzNZx")

(defn rdio-query [query]
  (first
  (get-in
  (json/parse-string
    (:body
      (http/post
      (str rdio-base-url "search" )
      {:query-params {"query" query
                      "method" "search"
                      "types" "track" }
        :throw-exceptions false
        :headers {"Authorization" token }})))
    ["result" "results"])))

(defonce tracks (format-tracks (current-tracks)))

(defn rdio-playlists []
  (get-in
    (json/parse-string
     (:body
       (http/post
         (str rdio-base-url "getPlaylists")
         {:headers {"Authorization" token}
          :query-params {"method" "getPlaylists"}})))
     ["result" "owned"]))

(defn current-playlist-name []
  (str
   "Open Air "
   (.format (java.text.SimpleDateFormat. "MMMM d, yyyy") (java.util.Date.))))

(current-playlist-name)

(defn current-playlist []
  (first
   (filter
   (fn [p]
     (= (current-playlist-name)
        (get p "name")))
   (rdio-playlists))))

(defn todays-playlist []
  (if-let [pl (current-playlist)]
    pl
    "To do - create the playlist"))

(todays-playlist)

; 1 - find all recent tracks
; 2 - create a playlist for today
; 3 - add tracks for today

; (filter (fn [x] (= 6 x)) (range 10))

(let [a "Aaron" b "Horace" c "Dan"]
  (println a)
  (println b)
  (println c))
