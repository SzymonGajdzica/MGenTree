package main.link

import main.data.*

class FamilyLinkProviderHolderImpl : FamilyLinkProviderHolder {

    override val providers: Set<FamilyLinkProvider> = setOf(
        Father.provider,
        Brother.provider,
        Uncle.provider,
        Husband.provider,
        BrotherInLaw.provider,
    )

}