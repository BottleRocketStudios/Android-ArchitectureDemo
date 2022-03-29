package com.bottlerocketstudios.brarchitecture.domain.models

/**
 * [Marker interface](https://en.wikipedia.org/wiki/Marker_interface_pattern) for all [DomainModel]s used in the app to quickly/easily find them.
 *
 * [DomainModel]s exist to be a primary representation of data that is used throughout the app that has already been preprocessed (deserialized) from some "dirty" model such as the network/database/etc.
 * [DomainModel]s shouldn't have json serialization/deserialization logic (that should be represented by a [Dto]).
 *
 * > ... a business object (Domain Model) usually does nothing itself but holds a set of instance variables or properties, also known as attributes, and associations with other business objects,
 * weaving a map of objects representing the business relationships.
 *
 * More info at:
 * * https://confluence.bottlerocketapps.com/display/BKB/Common+Code+Quality+Issues#CommonCodeQualityIssues-Datamodels
 * * https://en.wikipedia.org/wiki/Domain_model
 * * https://www.martinfowler.com/eaaCatalog/domainModel.html
 * * https://en.wikipedia.org/wiki/Business_object
 *
 * See [Dto] for related information.
 */
interface DomainModel
