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
	        "tokenizer" : {
	            "springSenseTokenizer" : {
	                "type" : "org.elasticsearch.index.analysis.springsense.SpringSenseTokenizerFactory"
	            }    
	        }           
	    }
        }
} 

mapping for multi field one standard one spring sense




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
