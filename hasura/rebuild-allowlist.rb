#!/usr/bin/env ruby

require 'pathname'
require 'yaml'
require 'json'
require 'pp'


class HasuraMetadataEditor
  def initialize(hasura_config_root:, api_spec_root:)
    @hasura_config_root = hasura_config_root
    @api_spec_root = api_spec_root

    @allow_list = YAML.safe_load hasura_allowlist_file.read
    @query_collections = YAML.safe_load(hasura_query_collection_file.read, permitted_classes: [Symbol])
    @exported_queries = JSON.load graphql_query_file.read
  end

  def apply
    allowed_queries = @query_collections[0]
    if allowed_queries['name'] != 'allowed-queries'
      pp @query_collections
      fail "collection 'allowed-queries' not found"
    end

    allowed_queries['definition']['queries'] = @exported_queries.map{|hash, query| {'name'=>"query#{hash}", 'query'=>query}}

    pp allowed_queries

    hasura_query_collection_file.write YAML.dump(@query_collections)
  end


  def read_graphql_queries
    JSON.load(graphql_query_file.read)
  end

  private def graphql_query_file
    @api_spec_root / 'relay-queries.json'
  end

  def hasura_allowlist_file
    @hasura_config_root / 'metadata' / 'allow_list.yaml'
  end

  def hasura_query_collection_file
    @hasura_config_root / 'metadata' / 'query_collections.yaml'
  end


end


editor = HasuraMetadataEditor.new(
  hasura_config_root: Pathname.new(__FILE__).parent,
  api_spec_root: Pathname.new(__FILE__).parent.parent / 'api-spec',
  )

editor.apply
