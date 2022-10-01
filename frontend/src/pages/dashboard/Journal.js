import { useState } from "react"
import { FormRow, FormRowSelect, JournalContainer } from "../../components"
import { useAppContext } from '../../context/appContext'
import { default as SearchWrapper} from '../../assets/wrappers/SearchContainer'

const Journal = () => {
    const {
        isLoading,
        searchKeyword,
        searchJournalType,
        journalSearchOptions,
        handleChange,
        journalSearch,
      } = useAppContext()

    const handleInputChange = (e) => {
        if (isLoading) return
        handleChange({ name: e.target.name, value: e.target.value })
    }

    const handleSearch = (e) => {
        e.preventDefault()
        journalSearch();
    }

    return (
      <div>
        <SearchWrapper>
          <form className="form">
            <FormRow
              labelText="Keyword"
              type="text"
              name="searchKeyword"
              value={searchKeyword}
              handleChange={handleInputChange}
            />
            <FormRowSelect
              labelText="Type"
              name="searchJournalType"
              value={searchJournalType}
              handleChange={handleInputChange}
              list={[...journalSearchOptions]}
            />
            <button className="btn" disabled={isLoading} onClick={handleSearch}>
              Search
            </button>
          </form>
        </SearchWrapper>
        <JournalContainer />
        {/* {searchJournalType === "Journal" ? <JournalContainer /> : <PaperContainer />} */}
      </div>
    );
    }
export default Journal