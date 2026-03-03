package com.bity.icp_kotlin_kit.data.service

import com.bity.icp_kotlin_kit.data.service.nft.EXTNFTRepository
import com.bity.icp_kotlin_kit.domain.service.NFTCollectionIdService //import com.bity.icp_kotlin_kit.di.nftCollectionIdService
import com.bity.icp_kotlin_kit.data.generated_file.EXTService
import com.bity.icp_kotlin_kit.data.generated_file.EXTService.UnnamedClass2
import com.bity.icp_kotlin_kit.data.generated_file.Metadata
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
//import com.bity.icp_kotlin_kit.util.MutantSpaceApesPrincipal
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


// TODO: After Phase 4.5 (crypto expect/actual), consider re-enabling real ICPPrincipal
//       construction here to test canonical text decoding end-to-end. For now, we mock
//       ICPPrincipal to isolate EXTNFTRepository from unfinished crypto.

class EXTNFTServiceTest {

    private val canister = mockk<ICPPrincipal>(relaxed = true) // private val canister = mockk<ICPPrincipal>()
    private val extService = mockk<EXTService>()
    private val idService = mockk<NFTCollectionIdService>(relaxed = true)
    private lateinit var service: EXTNFTRepository

    @BeforeEach
    fun setUp() {
        service = EXTNFTRepository(
            canister = canister,
            service = extService,
            idService = idService
        )
    }

    @Test
    fun `fetchCollectionNFTs Mutant Space Apes`() = runTest {
        val collectionPrincipal = mockk<ICPPrincipal>(relaxed = true)
        coEvery { collectionPrincipal.string } returns "gikg4-eaaaa-aaaam-qaieq-cai"
        coEvery { collectionPrincipal.bytes } returns byteArrayOf(1, 2, 3)

        coEvery { canister.bytes } returns byteArrayOf(1, 2, 3) //coEvery { canister.bytes } returns MutantSpaceApesPrincipal.bytes
        coEvery { canister.string } returns "gikg4-eaaaa-aaaam-qaieq-cai" //coEvery { canister.string } returns MutantSpaceApesPrincipal.string
        coEvery { extService.getTokens(any(), any(), any()) } returns arrayOf(
            UnnamedClass2(
                tokenIndex = 0U,
                metadata = Metadata.nonfungible(null)
            ),
            UnnamedClass2(
                tokenIndex = 1U,
                metadata = Metadata.nonfungible(null)
            ),
            UnnamedClass2(
                tokenIndex = 373U,
                metadata = Metadata.nonfungible(null)
            )
        )

        coEvery {
            idService.getNFTCollectionItemId(any(), ICPBigInteger.ZERO)
        } returns "jkgnc-hakor-uwiaa-aaaaa-deacb-eaqca-aaaaa-a"

        coEvery {
            idService.getNFTCollectionItemId(any(), ICPBigInteger.ONE)
        } returns "hwf6d-cqkor-uwiaa-aaaaa-deacb-eaqca-aaaaa-q"

        coEvery {
            idService.getNFTCollectionItemId(any(), ICPBigInteger.parseDecimal("373"))
        } returns "op4gl-3qkor-uwiaa-aaaaa-deacb-eaqca-aaaf2-q"

        val nftCollection = service.fetchNFTs(collectionPrincipal)

        assertEquals(3, nftCollection.size)

        assertEquals(ICPBigInteger.ZERO, nftCollection.first().id)
        assertEquals("jkgnc-hakor-uwiaa-aaaaa-deacb-eaqca-aaaaa-a", nftCollection.first().nftId)
        assertEquals("https://gikg4-eaaaa-aaaam-qaieq-cai.raw.icp0.io/?tokenid=jkgnc-hakor-uwiaa-aaaaa-deacb-eaqca-aaaaa-a", nftCollection.first().metadata?.thumbnailUrl)

        assertEquals(ICPBigInteger.ONE, nftCollection[1].id)
        assertEquals("hwf6d-cqkor-uwiaa-aaaaa-deacb-eaqca-aaaaa-q", nftCollection[1].nftId)
        assertEquals("https://gikg4-eaaaa-aaaam-qaieq-cai.raw.icp0.io/?tokenid=hwf6d-cqkor-uwiaa-aaaaa-deacb-eaqca-aaaaa-q", nftCollection[1].metadata?.thumbnailUrl)

        assertEquals(ICPBigInteger.parseDecimal("373"), nftCollection.last().id)
        assertEquals("op4gl-3qkor-uwiaa-aaaaa-deacb-eaqca-aaaf2-q", nftCollection.last().nftId)
        assertEquals("https://gikg4-eaaaa-aaaam-qaieq-cai.raw.icp0.io/?tokenid=op4gl-3qkor-uwiaa-aaaaa-deacb-eaqca-aaaf2-q", nftCollection.last().metadata?.thumbnailUrl)
    }

}