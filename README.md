SpringSense for ElasticSearch
===================================

request to create index with 1 shard and registered SpringSense anayizer

{"settings": { 
"index.number_of_shards":"1",
"analysis" : {
	        "analyzer" : {
	            "springSenseAnalyzer" : {
	                "type" : "custom",
	                "tokenizer" : "springSenseTokenizer"
	            }    
	        },
	        "analyzer" : {
	            "lowecaseSpringSenseAnalyzer" : {
	                "type" : "custom",
	                "tokenizer" : "lowercaseSpringSenseTokenizer"
	            }    
	        },        
	        "tokenizer" : {
	            "springSenseTokenizer" : {
	                "type" : "org.elasticsearch.index.analysis.springsense.SpringSenseTokenizerFactory"
	            }    
	        } ,        
	        "tokenizer" : {
	            "lowercaseSpringSenseTokenizer" : {
	                "type" : "org.elasticsearch.index.analysis.springsense.SpringSenseTokenizerFactory" ,
	                "transform_to_lowercase" : true
	            }    
	        }           
	    }
        }
} 

mapping for multi field one standard one spring sense
{
    "tweet" : {
        "properties": {
            "message": {
                "type": "multi_field",
                "path": "just_name",
                "fields": {
                    "standard": {"type": "string", "index": "analyzed",  "analyzer" : "standard"},
                    "spring-sense": {"type": "string","index": "analyzed", "analyzer" : "springSenseAnalyzer"},
                    "spring-sense-lowercase": {"type": "string","index": "analyzed", "analyzer" : "lowecaseSpringSenseAnalyzer"}
                }
            }
        }
    }
}

 standard mapping from manifoldCf into elasticsearch
 
 
 {
"website":{
"properties":{
"file":{
"type":"string"
},
"header-Cache-Control":{
"type":"string"
},
"header-Connection":{
"type":"string"
},
"header-Content-Type":{
"type":"string"
},
"header-Expires":{
"type":"string"
},
"header-Link":{
"type":"string"
},
"header-Pragma":{
"type":"string"
},
"header-Server":{
"type":"string"
},
"header-Transfer-Encoding":{
"type":"string"
},
"header-X-Pingback":{
"type":"string"
},
"header-X-Powered-By":{
"type":"string"
},
"type":{
"type":"string"
}
}
}
}



License
-------

    This software is licensed under the Apache 2 license, quoted below.

    Copyright 2009-2012 Shay Banon and ElasticSearch <http://www.elasticsearch.org>

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
