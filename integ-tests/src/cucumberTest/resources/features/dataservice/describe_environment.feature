@dataservice
@describe-environment
Feature: Describe environment

  Scenario: Describe a newly created environment
    Given I create an environment named "test"
    When I describe the created environment
    Then the created and described environments match

  Scenario: Describe a non-existent environment
    When I try to describe a non-existent environment named "non-existent"
    Then there should be a ResourceNotFoundException thrown
    And its resourceType is "environment"
    And its resourceId contains "non-existent"

  @ignore
  Scenario: Describe a deleted environment
    Given I create an environment named "test"
    And I delete the created environment
    When I try to describe the created environment
    Then there should be a ResourceNotFoundException thrown
    And its resourceType is "environment"
    And its resourceId contains "test"

  #TODO: Add invalid parameter tests